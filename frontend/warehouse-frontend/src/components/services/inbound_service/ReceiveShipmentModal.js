"use client";
import React, { useState, useEffect } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
  Box,
  Autocomplete,
  CircularProgress,
  Alert,
} from "@mui/material";
import {
  receiveGoods,
  getAvailableSuppliers,
  getAvailableProducts,
} from "@/services/inbound_service/inboundApi";

export default function ReceiveShipmentModal({ open, onClose, onRefresh }) {
  const [suppliers, setSuppliers] = useState([]);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [form, setForm] = useState({
    supplierName: "",
    productName: "",
    sku: "",
    quantity: 1,
  });

  useEffect(() => {
    if (open) {
      const loadOptions = async () => {
        setLoading(true);
        setError(""); // Clear previous errors
        try {
          // Fetch both Suppliers and Products in parallel
          const [prodRes, supRes] = await Promise.all([
            getAvailableProducts(),
            getAvailableSuppliers(),
          ]);

          // Alphabetical Sort for Products
          const sortedProds = (prodRes.data || []).sort((a, b) =>
            (a.productName || "").localeCompare(b.productName || "")
          );

          // Alphabetical Sort for Suppliers
          const sortedSups = (supRes.data || []).sort((a, b) =>
            (a.supplierName || "").localeCompare(b.supplierName || "")
          );

          setProducts(sortedProds);
          setSuppliers(sortedSups);
        } catch (err) {
          console.error("Link to Microservices failed", err);
          setError("Could not load data from linked services.");
        } finally {
          setLoading(false);
        }
      };
      loadOptions();
    }
  }, [open]);

  const handleSubmit = async () => {
    if (form.quantity <= 0) {
      setError("Quantity must be a positive number.");
      return;
    }
    if (!form.productName || !form.supplierName) {
      setError("Please select both a supplier and a product.");
      return;
    }
    try {
      await receiveGoods(form); // POST /api/v1/inbound/receive
      onRefresh();
      onClose();
      // Reset form on success
      setForm({ supplierName: "", productName: "", sku: "", quantity: 1 });
    } catch (err) {
      setError("Communication with remote service failed.");
    }
  };

  return (
    <Dialog
      open={open}
      onClose={onClose}
      fullWidth
      maxWidth="xs"
      PaperProps={{ sx: { borderRadius: 3 } }}
    >
      <DialogTitle sx={{ fontWeight: 700, px: 3, pt: 3 }}>
        Receive New Shipment
      </DialogTitle>
      <DialogContent sx={{ px: 3 }}>
        {loading ? (
          <Box sx={{ display: "flex", justifyContent: "center", p: 3 }}>
            <CircularProgress size={24} />
          </Box>
        ) : (
          <Box sx={{ display: "flex", flexDirection: "column", gap: 2.5, mt: 1 }}>
            {error && (
              <Alert severity="error" sx={{ borderRadius: 2 }}>
                {error}
              </Alert>
            )}

            {/* Searchable Supplier Dropdown */}
            <Autocomplete
              options={suppliers}
              getOptionLabel={(option) => option.supplierName || ""}
              // Ensure we check for null/undefined value
              value={suppliers.find((s) => s.supplierName === form.supplierName) || null}
              onChange={(e, val) =>
                setForm({ ...form, supplierName: val?.supplierName || "" })
              }
              renderInput={(params) => (
                <TextField {...params} label="Select Supplier" fullWidth />
              )}
            />

            {/* Searchable Product Dropdown + SKU Auto-fill */}
            <Autocomplete
              options={products}
              getOptionLabel={(option) => option.productName || ""}
              // Ensure we check for null/undefined value
              value={products.find((p) => p.productName === form.productName) || null}
              onChange={(e, val) => {
                // This logic "Reconnects" the SKU by pulling it from the Product API data
                setForm({
                  ...form,
                  productName: val?.productName || "",
                  sku: val?.sku || "", // Auto-fills the SKU field
                });
                if (val) setError("");
              }}
              renderInput={(params) => (
                <TextField {...params} label="Select Product" fullWidth />
              )}
            />

            <TextField
              label="SKU (Auto-filled)"
              fullWidth
              value={form.sku}
              InputProps={{ readOnly: true }}
              disabled
              variant="filled"
              helperText={!form.sku ? "Select a product to see SKU" : ""}
            />

            <TextField
              label="Quantity"
              type="number"
              fullWidth
              inputProps={{ min: 1 }}
              value={form.quantity}
              onChange={(e) => {
                const val = parseInt(e.target.value) || 0;
                setForm({ ...form, quantity: val });
                if (val > 0) setError("");
              }}
            />
          </Box>
        )}
      </DialogContent>
      <DialogActions sx={{ p: 3 }}>
        <Button
          onClick={onClose}
          sx={{ color: "#64748b", textTransform: "none" }}
        >
          Cancel
        </Button>
        <Button
          variant="contained"
          onClick={handleSubmit}
          sx={{ bgcolor: "#6366f1", borderRadius: 2, textTransform: "none" }}
        >
          Create Receipt
        </Button>
      </DialogActions>
    </Dialog>
  );
}