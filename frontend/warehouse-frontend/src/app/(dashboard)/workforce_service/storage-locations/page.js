"use client";

import { useEffect, useState, useCallback } from "react";
import AddIcon from "@mui/icons-material/Add";
import DeleteIcon from "@mui/icons-material/Delete";
import LocationOnIcon from "@mui/icons-material/LocationOn";
import { Box, Button, IconButton, Tooltip } from "@mui/material";

import PageHeader from "@/components/workforce/PageHeader";
import DataTable from "@/components/workforce/DataTable";
import WorkerStorageLocationFormDialog from "@/components/workforce/WorkerStorageLocationFormDialog";
import {
  LoadingState,
  EmptyState,
  ConfirmDialog,
  Toast,
} from "@/components/workforce/shared";
import {
  getAllWorkers,
  assignWorkerToStorageLocation,
  getStorageLocationsByWorkerId,
  removeWorkerStorageLocation,
} from "@/services/workforce";

const formatDate = (dt) => {
  if (!dt) return "—";
  return new Date(dt).toLocaleString("en-US", {
    year: "numeric",
    month: "short",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
};

export default function WorkerStorageLocationsPage() {
  const [assignments, setAssignments] = useState([]);
  const [workers, setWorkers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [formOpen, setFormOpen] = useState(false);
  const [saving, setSaving] = useState(false);
  const [deleteTarget, setDeleteTarget] = useState(null);
  const [deleting, setDeleting] = useState(false);
  const [toast, setToast] = useState({
    open: false,
    message: "",
    severity: "success",
  });

  const fetchData = useCallback(async () => {
    try {
      setLoading(true);
      const wRes = await getAllWorkers();
      setWorkers(wRes.data);

      // Fetch storage location assignments for all workers
      const allAssignments = [];
      for (const worker of wRes.data) {
        try {
          const res = await getStorageLocationsByWorkerId(worker.id);
          const mapped = res.data.map((a) => ({
            ...a,
            workerName: worker.name,
          }));
          allAssignments.push(...mapped);
        } catch {
          // Worker may have no assignments, skip
        }
      }
      setAssignments(allAssignments);
    } catch {
      setToast({
        open: true,
        message: "Failed to load data",
        severity: "error",
      });
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const handleCreate = () => {
    setFormOpen(true);
  };

  const handleFormSubmit = async (data) => {
    try {
      setSaving(true);
      await assignWorkerToStorageLocation(data);
      setToast({
        open: true,
        message: "Worker assigned to storage location",
        severity: "success",
      });
      setFormOpen(false);
      fetchData();
    } catch (err) {
      const msg = err.response?.data?.message || "Operation failed";
      setToast({ open: true, message: msg, severity: "error" });
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async () => {
    try {
      setDeleting(true);
      await removeWorkerStorageLocation(deleteTarget.id);
      setToast({
        open: true,
        message: "Assignment removed",
        severity: "success",
      });
      setDeleteTarget(null);
      fetchData();
    } catch (err) {
      const msg = err.response?.data?.message || "Delete failed";
      setToast({ open: true, message: msg, severity: "error" });
    } finally {
      setDeleting(false);
    }
  };

  const columns = [
    { id: "id", label: "ID", sortable: true },
    { id: "workerName", label: "Worker", sortable: true },
    { id: "storageLocationId", label: "Storage Location ID", sortable: true },
    {
      id: "assignedDate",
      label: "Assigned Date",
      sortable: true,
      render: (row) => formatDate(row.assignedDate),
    },
    {
      id: "actions",
      label: "Actions",
      sortable: false,
      align: "right",
      render: (row) => (
        <Box sx={{ display: "flex", justifyContent: "flex-end", gap: 0.5 }}>
          <Tooltip title="Remove Assignment">
            <IconButton
              size="small"
              onClick={(e) => {
                e.stopPropagation();
                setDeleteTarget(row);
              }}
              sx={{ color: "#ef4444" }}
            >
              <DeleteIcon fontSize="small" />
            </IconButton>
          </Tooltip>
        </Box>
      ),
    },
  ];

  if (loading) return <LoadingState message="Loading storage location assignments..." />;

  return (
    <Box>
      <PageHeader
        title="Worker Storage Locations"
        subtitle="Assign workers to storage locations from the Inventory service."
        icon={<LocationOnIcon sx={{ fontSize: 32 }} />}
        backHref="/workforce_service"
        count={assignments.length}
        action={
          <Button
            variant="contained"
            startIcon={<AddIcon />}
            onClick={handleCreate}
            sx={{ bgcolor: "#6366f1", "&:hover": { bgcolor: "#4f46e5" } }}
          >
            Assign Worker
          </Button>
        }
      />

      <DataTable
        columns={columns}
        rows={assignments}
        searchKeys={["workerName", "storageLocationId"]}
        emptyComponent={
          <EmptyState
            icon={<LocationOnIcon />}
            message="No storage location assignments found."
          />
        }
      />

      <WorkerStorageLocationFormDialog
        open={formOpen}
        onClose={() => setFormOpen(false)}
        onSubmit={handleFormSubmit}
        workers={workers}
        loading={saving}
      />

      <ConfirmDialog
        open={Boolean(deleteTarget)}
        title="Remove Assignment"
        message="Are you sure you want to remove this worker-storage location assignment?"
        onConfirm={handleDelete}
        onCancel={() => setDeleteTarget(null)}
        loading={deleting}
      />

      <Toast
        open={toast.open}
        message={toast.message}
        severity={toast.severity}
        onClose={() => setToast({ ...toast, open: false })}
      />
    </Box>
  );
}
