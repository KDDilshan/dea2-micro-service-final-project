"use client";

import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  MenuItem,
  TextField,
  CircularProgress,
  Box,
  Grid,
} from "@mui/material";
import { useEffect, useState } from "react";

const initialForm = {
  workerId: "",
  storageLocationId: "",
};

export default function WorkerStorageLocationFormDialog({
  open,
  onClose,
  onSubmit,
  workers = [],
  loading,
}) {
  const [form, setForm] = useState(initialForm);
  const [errors, setErrors] = useState({});
  const [locationInput, setLocationInput] = useState("");

  useEffect(() => {
    setForm(initialForm);
    setLocationInput("");
    setErrors({});
  }, [open]);

  const validate = () => {
    const e = {};
    if (!form.workerId) e.workerId = "Worker is required";
    if (!locationInput || isNaN(locationInput))
      e.storageLocationId = "Valid Storage Location ID is required";
    setErrors(e);
    return Object.keys(e).length === 0;
  };

  const handleSubmit = () => {
    if (!validate()) return;
    onSubmit({
      workerId: form.workerId,
      storageLocationId: Number(locationInput),
    });
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle sx={{ fontWeight: 600 }}>
        Assign Worker to Storage Location
      </DialogTitle>
      <DialogContent>
        <Box sx={{ pt: 1 }}>
          <Grid container spacing={2}>
            <Grid size={{ xs: 12, sm: 6 }}>
              <TextField
                label="Worker"
                select
                fullWidth
                value={form.workerId}
                onChange={(e) =>
                  setForm({ ...form, workerId: e.target.value })
                }
                error={!!errors.workerId}
                helperText={errors.workerId}
              >
                {workers.map((w) => (
                  <MenuItem key={w.id} value={w.id}>
                    {w.name}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>
            <Grid size={{ xs: 12, sm: 6 }}>
              <TextField
                label="Storage Location ID"
                type="number"
                fullWidth
                value={locationInput}
                onChange={(e) => setLocationInput(e.target.value)}
                error={!!errors.storageLocationId}
                helperText={errors.storageLocationId}
              />
            </Grid>
          </Grid>
        </Box>
      </DialogContent>
      <DialogActions sx={{ px: 3, pb: 2 }}>
        <Button onClick={onClose} disabled={loading} sx={{ color: "#64748b" }}>
          Cancel
        </Button>
        <Button
          onClick={handleSubmit}
          variant="contained"
          disabled={loading}
          startIcon={loading ? <CircularProgress size={16} /> : null}
          sx={{ bgcolor: "#6366f1", "&:hover": { bgcolor: "#4f46e5" } }}
        >
          Assign
        </Button>
      </DialogActions>
    </Dialog>
  );
}
