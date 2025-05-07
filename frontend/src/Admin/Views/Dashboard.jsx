import React from 'react';
import Grid from "@mui/material/Grid";
import AdminPannel from "../../Styles/AdminPannelWrapper";
import Achivement from "../tables/Achivement";
import MonthlyOverview from "../tables/MonthlyOverView";
import WeeklyOverview from "../tables/WeeklyOverview";
import YearlyStats from "../tables/TotalEarning";
import QuarterlyRevenueChart from "../tables/QuarterlyRevenueChart";
import ProductCategoryDistribution from "../tables/ProductCategoryDistribution";
import SalesByCountries from "../tables/SalesByContry";
import DepositWithdraw from "../tables/DepositeAndWithdraw";
import CustomersTable from "../tables/CustomersTable";
import { ThemeProvider } from "@mui/material";
import { customTheme } from "../them/customeThem";
import "./Admin.css";
import RecentlyAddeddProducts from "../tables/RecentlyAddeddProducts";
import SalesOverTime from "../tables/SalesOverTime";
import RecentOrders from "../tables/RecentOrders";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import WeeklyStats from "./WeeklyStats";
import Box from '@mui/material/Box';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import Divider from '@mui/material/Divider';

const Dashboard = () => {
  const { auth } = useSelector(store => store);
  const navigate = useNavigate();

  return (
    <div className="adminContainer">
      <ThemeProvider theme={customTheme}>
        <AdminPannel>
          <Grid container spacing={3}>
            {/* First Row: Overview and Weekly Stats */}
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

            {/* Second Row: Yearly Stats and Category Distribution */}
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

            {/* Third Row: Recent Orders and Customers */}
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

            {/* Fourth Row: Recently Added Products and Customers */}
            <Grid item xs={12} md={8}>
              <Card sx={{ height: '100%' }}>
                <CardContent>
                  {/* <Typography variant="h6" gutterBottom>
                    Products
                  </Typography> */}
                  <RecentlyAddeddProducts />
                </CardContent>
              </Card>
            </Grid>
            <Grid item xs={12} md={4}>
              <Card sx={{ height: '100%' }}>
                <CardContent>
                  {/* <Typography variant="h6" gutterBottom>
                    New Customers
                  </Typography> */}
                  <CustomersTable />
                </CardContent>
              </Card>
            </Grid>

            {/* Additional components that are currently commented out but can be added back */}
            {/* 
            <Grid item xs={12} md={4}>
              <Card sx={{ height: '100%' }}>
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    Achievements
                  </Typography>
                  <Achivement />
                </CardContent>
              </Card>
            </Grid>

            <Grid item xs={12} md={4}>
              <Card sx={{ height: '100%' }}>
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    Weekly Overview
                  </Typography>
                  <WeeklyOverview />
                </CardContent>
              </Card>
            </Grid>

            <Grid item xs={12} md={8}>
              <Card sx={{ height: '100%' }}>
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    Sales Trends
                  </Typography>
                  <SalesOverTime />
                </CardContent>
              </Card>
            </Grid>

            <Grid item xs={12}>
              <Card>
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    Quarterly Performance Trends
                  </Typography>
                  <QuarterlyRevenueChart />
                </CardContent>
              </Card>
            </Grid>

            <Grid item xs={12} md={4}>
              <Card sx={{ height: '100%' }}>
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    Sales by Country
                  </Typography>
                  <SalesByCountries />
                </CardContent>
              </Card>
            </Grid>

            <Grid item xs={12}>
              <Card>
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    Financial Activity
                  </Typography>
                  <DepositWithdraw />
                </CardContent>
              </Card>
            </Grid>
            */}
          </Grid>
        </AdminPannel>
      </ThemeProvider>
    </div>
  );
};

export default Dashboard;