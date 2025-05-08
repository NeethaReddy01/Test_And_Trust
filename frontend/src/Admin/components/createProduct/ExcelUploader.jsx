import { useState, useEffect } from "react";
import { Button, Typography, Box, CircularProgress, Alert } from "@mui/material";
import * as XLSX from "xlsx";
import { useDispatch, useSelector } from "react-redux";
import { createMultipleProducts } from "../../../Redux/Admin/Product/Action";

const ExcelUploader = () => {
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState({ text: "", type: "" });
  const dispatch = useDispatch();
  const jwt = localStorage.getItem("jwt");
  
  // Fixed selector to use adminsProduct instead of product
  const { loading: reduxLoading, uploadSuccess, uploadError } = useSelector(
    (state) => state.adminsProduct || {}
  );
  
  // Update local state based on Redux state
  useEffect(() => {
    if (uploadSuccess) {
      setMessage({ text: uploadSuccess, type: "success" });
      setLoading(false);
    }
    if (uploadError) {
      setMessage({ text: uploadError, type: "error" });
      setLoading(false);
    }
  }, [uploadSuccess, uploadError]);

  const handleFileUpload = (event) => {
    const file = event.target.files[0];
    if (!file) return;

    // Check if file is Excel
    const fileExtension = file.name.split('.').pop().toLowerCase();
    if (fileExtension !== 'xlsx' && fileExtension !== 'xls') {
      setMessage({ text: "Please upload only Excel files (.xlsx or .xls)", type: "error" });
      return;
    }

    setLoading(true);
    setMessage({ text: "", type: "" });

    const reader = new FileReader();
    reader.onload = (e) => {
      try {
        const data = new Uint8Array(e.target.result);
        const workbook = XLSX.read(data, { type: 'array' });
        const worksheet = workbook.Sheets[workbook.SheetNames[0]];
        const jsonData = XLSX.utils.sheet_to_json(worksheet);

        if (jsonData.length === 0) {
          setMessage({ text: "Excel file is empty", type: "error" });
          setLoading(false);
          return;
        }

        // Process and validate data
        const formattedProducts = jsonData.map((row, index) => {
          // Convert Excel data to match your product structure
          return {
            imageUrl: row.imageUrl || "",
            brand: row.brand || "",
            title: row.title || "",
            color: row.color || "",
            discountedPrice: row.discountedPrice ? row.discountedPrice.toString() : "",
            price: row.price ? row.price.toString() : "",
            discountPersent: row.discountPersent ? row.discountPersent.toString() : "",
            sizes: row.sizes || "",
            quantity: row.quantity ? row.quantity.toString() : "",
            level1Category: row.level1Category || "",
            level2Category: row.level2Category || "",
            description: row.description || ""
          };
        });

        // Use Redux action to submit products
        handleSubmitProducts(formattedProducts);
      } catch (error) {
        console.error("Error processing Excel file:", error);
        setMessage({ text: "Error processing Excel file: " + error.message, type: "error" });
        setLoading(false);
      }
    };

    reader.onerror = () => {
      setMessage({ text: "Error reading file", type: "error" });
      setLoading(false);
    };

    reader.readAsArrayBuffer(file);
  };

  const handleSubmitProducts = (products) => {
    // Dispatch Redux action for multiple products
    dispatch(createMultipleProducts(products, jwt))
      .catch(error => {
        console.error("Error in Redux action:", error);
      });
  };

  const triggerFileInput = () => {
    document.getElementById("excel-file-input").click();
  };

  return (
    <Box sx={{ ml: 1.5 }}>
      <input
        type="file"
        id="excel-file-input"
        accept=".xlsx, .xls"
        style={{ display: "none" }}
        onChange={handleFileUpload}
      />
      {/* <Button
                      variant="contained"
                      sx={{ p: 0.7 }}
                      color="primary"
                      size="large"
                      type="submit"
                    >
                      Add New Product
                    </Button> */}
      <Button
        variant="contained"
        color="primary"
        size="large"
        onClick={triggerFileInput}
        disabled={loading}
        sx={{ p: 0.7 }}
      >
        {loading ? <CircularProgress size={24} /> : "Upload from Excel"}
      </Button>

      {message.text && (
        <Alert severity={message.type} sx={{ mt: 2 }}>
          {message.text}
        </Alert>
      )}
      
     
    </Box>
  );
};

export default ExcelUploader;