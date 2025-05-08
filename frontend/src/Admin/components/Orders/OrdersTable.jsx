import {
  Avatar,
  AvatarGroup,
  Box,
  Button,
  Card,
  CardHeader,
  Chip,
  Menu,
  MenuItem,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";

import React, { useEffect, useState } from "react";

import { useDispatch, useSelector } from "react-redux";
import {
  confirmOrder,
  deleteOrder,
  deliveredOrder,
  getOrders,
  shipOrder,
} from "../../../Redux/Admin/Orders/Action";

const OrdersTable = () => {
  const [formData, setFormData] = useState({ status: "", sort: "" });
  const dispatch = useDispatch();
  const jwt = localStorage.getItem("jwt");
  const { adminsOrder } = useSelector((store) => store);
  const [anchorElArray, setAnchorElArray] = useState([]);
  const [isLoading, setIsLoading] = useState(false);

  // Track status changes in Redux state to trigger UI updates
  const orderStatusChanged = useSelector((store) => ({
    delivered: store.adminsOrder?.delivered,
    shipped: store.adminsOrder?.shipped,
    confirmed: store.adminsOrder?.confirmed,
    deleted: store.adminsOrder?.deleted
  }));

  // Fetch orders when component mounts or when any status changes
  useEffect(() => {
    dispatch(getOrders({ jwt }));
  }, [
    jwt, 
    dispatch, 
    orderStatusChanged.delivered, 
    orderStatusChanged.shipped, 
    orderStatusChanged.confirmed,
    orderStatusChanged.deleted
  ]);

  const handleUpdateStatusMenuClick = (event, index) => {
    const newAnchorElArray = [...anchorElArray];
    newAnchorElArray[index] = event.currentTarget;
    setAnchorElArray(newAnchorElArray);
  };

  const handleUpdateStatusMenuClose = (index) => {
    const newAnchorElArray = [...anchorElArray];
    newAnchorElArray[index] = null;
    setAnchorElArray(newAnchorElArray);
  };

  const handleChange = (event) => {
    const name = event.target.name;
    const value = event.target.value;
    setFormData({ ...formData, [name]: value });
  };

  const handleConfirmedOrder = async (orderId, index) => {
    setIsLoading(true);
    handleUpdateStatusMenuClose(index);
    await dispatch(confirmOrder(orderId));
    setIsLoading(false);
  };

  const handleShippedOrder = async (orderId, index) => {
    setIsLoading(true);
    handleUpdateStatusMenuClose(index);
    await dispatch(shipOrder(orderId));
    setIsLoading(false);
  };

  const handleDeliveredOrder = async (orderId, index) => {
    setIsLoading(true);
    handleUpdateStatusMenuClose(index);
    await dispatch(deliveredOrder(orderId));
    setIsLoading(false);
  };

  const handleDeleteOrder = async (orderId, index) => {
    setIsLoading(true);
    handleUpdateStatusMenuClose(index);
    await dispatch(deleteOrder(orderId));
    setIsLoading(false);
  };

  return (
    <Box>
      <Card className="mt-2">
        <CardHeader
          title="All Orders"
          sx={{
            pt: 2,
            alignItems: "center",
            "& .MuiCardHeader-action": { mt: 0.6 },
          }}
        />
        <TableContainer>
          <Table sx={{ minWidth: 800 }} aria-label="table in dashboard">
            <TableHead>
              <TableRow>
                <TableCell>Image</TableCell>
                <TableCell>Title</TableCell>
                <TableCell>Price</TableCell>
                <TableCell>Id</TableCell>
                <TableCell sx={{ textAlign: "center" }}>Status</TableCell>
                <TableCell sx={{ textAlign: "center" }}>Update</TableCell>
                <TableCell sx={{ textAlign: "center" }}>Delete</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {adminsOrder?.orders?.map((item, index) => (
                <TableRow
                  hover
                  key={item.id || index}
                  sx={{ "&:last-of-type td, &:last-of-type th": { border: 0 } }}
                >
                  <TableCell>
                    <AvatarGroup max={4} sx={{ justifyContent: 'start' }}>
                      {item.orderItems.map((orderItem, idx) => (
                        <Avatar key={idx} alt={orderItem.product.title} src={orderItem.product.imageUrl} />
                      ))}
                    </AvatarGroup>
                  </TableCell>

                  <TableCell
                    sx={{ py: (theme) => `${theme.spacing(0.5)} !important` }}
                  >
                    <Box sx={{ display: "flex", flexDirection: "column" }}>
                      <Typography
                        sx={{
                          fontWeight: 500,
                          fontSize: "0.875rem !important",
                        }}
                      >
                        {item?.orderItems.map((order, idx) => (
                          <span key={idx}> {order.product.title}{idx < item.orderItems.length - 1 ? "," : ""}</span>
                        ))}
                      </Typography>
                      <Typography variant="caption">
                        {item?.orderItems.map((order, idx) => (
                          <span key={idx} className="opacity-60">
                            {" "}
                            {order.product.brand}{idx < item.orderItems.length - 1 ? "," : ""}
                          </span>
                        ))}
                      </Typography>
                    </Box>
                  </TableCell>

                  <TableCell>{item.totalPrice}</TableCell>
                  <TableCell>{item.id}</TableCell>
                  <TableCell className="text-white">
                    <Chip
                      sx={{
                        color: "white !important",
                        fontWeight: "bold",
                        textAlign: "center",
                      }}
                      label={item.orderStatus}
                      size="small"
                      color={
                        item.orderStatus === "PENDING" ? "info" : item.orderStatus === "DELIVERED" ? "success" : "secondary"
                      }
                      className="text-white"
                    />
                  </TableCell>
                  <TableCell
                    sx={{ textAlign: "center" }}
                    className="text-white"
                  >
                    <div>
                      <Button
                        id={`basic-button-${item.id}`}
                        aria-controls={`basic-menu-${item.id}`}
                        aria-haspopup="true"
                        aria-expanded={Boolean(anchorElArray[index])}
                        onClick={(event) =>
                          handleUpdateStatusMenuClick(event, index)
                        }
                        disabled={isLoading}
                      >
                        Status
                      </Button>
                      <Menu
                        id={`basic-menu-${item.id}`}
                        anchorEl={anchorElArray[index]}
                        open={Boolean(anchorElArray[index])}
                        onClose={() => handleUpdateStatusMenuClose(index)}
                        MenuListProps={{
                          "aria-labelledby": `basic-button-${item.id}`,
                        }}
                      >
                        <MenuItem
                          onClick={() => handleConfirmedOrder(item.id, index)}
                          disabled={isLoading || item.orderStatus === "DELIVERED" || item.orderStatus === "SHIPPED" || item.orderStatus === "CONFIRMED"}
                        >
                          CONFIRMED ORDER
                        </MenuItem>
                        <MenuItem
                          disabled={isLoading || item.orderStatus === "DELIVERED" || item.orderStatus === "SHIPPED"}
                          onClick={() => handleShippedOrder(item.id, index)}
                        >
                          SHIPPED ORDER
                        </MenuItem>
                        <MenuItem 
                          onClick={() => handleDeliveredOrder(item.id, index)}
                          disabled={isLoading || item.orderStatus === "DELIVERED"}
                        >
                          DELIVERED ORDER
                        </MenuItem>
                      </Menu>
                    </div>
                  </TableCell>
                  <TableCell
                    sx={{ textAlign: "center" }}
                    className="text-white"
                  >
                    <Button
                      onClick={() => handleDeleteOrder(item.id, index)}
                      variant="text"
                      disabled={isLoading}
                    >
                      delete
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Card>
    </Box>
  );
};

export default OrdersTable;