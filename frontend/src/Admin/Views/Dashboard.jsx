import React from 'react';
import Grid from "@mui/material/Grid";
import AdminPannel from "../../Styles/AdminPannelWrapper";
import MonthlyOverview from "../tables/MonthlyOverView";
import YearlyStats from "../tables/TotalEarning";
import ProductCategoryDistribution from "../tables/ProductCategoryDistribution";
import CustomersTable from "../tables/CustomersTable";
import { ThemeProvider } from "@mui/material";
import { customTheme } from "../them/customeThem";
import "./Admin.css";
import RecentlyAddeddProducts from "../tables/RecentlyAddeddProducts";
import RecentOrders from "../tables/RecentOrders";
import { useSelector } from "react-redux";
import WeeklyStats from "./WeeklyStats";
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';

const Dashboard = () => {
  const { auth } = useSelector(store => store);

  return (
    <div className="adminContainer">
      <ThemeProvider theme={customTheme}>
        <AdminPannel>
          <Grid container spacing={3}>
            <Grid item xs={12} md={8}>
              <Card sx={{ height: '100%' }}>
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    Monthly Overview
                  </Typography>
                  <MonthlyOverview />
                </CardContent>
              </Card>
            </Grid>
            <Grid item xs={12} md={4}>
              <Card sx={{ height: '100%' }}>
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    Weekly Performance
                  </Typography>
                  <WeeklyStats />
                </CardContent>
              </Card>
            </Grid>
            <Grid item xs={12} md={6}>
              <Card sx={{ height: '100%' }}>
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    Yearly Statistics
                  </Typography>
                  <YearlyStats />
                </CardContent>
              </Card>
            </Grid>
            <Grid item xs={12} md={6}>
              <Card sx={{ height: '100%' }}>
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    Category Distribution
                  </Typography>
                  <ProductCategoryDistribution />
                </CardContent>
              </Card>
            </Grid>
            <Grid item xs={12}>
              <Card>
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    Recent Orders
                  </Typography>
                  <RecentOrders />
                </CardContent>
              </Card>
            </Grid>
            <Grid item xs={12} md={8}>
              <Card sx={{ height: '100%' }}>
                <CardContent>
                  <RecentlyAddeddProducts />
                </CardContent>
              </Card>
            </Grid>
            <Grid item xs={12} md={4}>
              <Card sx={{ height: '100%' }}>
                <CardContent>
                  <CustomersTable />
                </CardContent>
              </Card>
            </Grid>
          </Grid>
        </AdminPannel>
      </ThemeProvider>
    </div>
  );
};

export default Dashboard;