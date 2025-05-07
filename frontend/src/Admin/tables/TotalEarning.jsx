// ** MUI Imports
import Box from '@mui/material/Box'
import Card from '@mui/material/Card'
import Avatar from '@mui/material/Avatar'
import Typography from '@mui/material/Typography'
import IconButton from '@mui/material/IconButton'
import CardHeader from '@mui/material/CardHeader'
import CardContent from '@mui/material/CardContent'
import LinearProgress from '@mui/material/LinearProgress'
import CircularProgress from '@mui/material/CircularProgress'
import { useEffect, useState } from 'react'
import axios from 'axios'
import Grid from '@mui/material/Grid'
import Divider from '@mui/material/Divider'
import Chip from '@mui/material/Chip'

// ** Icons Imports
import MenuUp from 'mdi-material-ui/MenuUp'
import MenuDown from 'mdi-material-ui/MenuDown'
import DotsVertical from 'mdi-material-ui/DotsVertical'
import CurrencyUsd from 'mdi-material-ui/CurrencyUsd'
import CartOutline from 'mdi-material-ui/CartOutline'
import AccountOutline from 'mdi-material-ui/AccountOutline'
import TrendingUp from 'mdi-material-ui/TrendingUp'
import { getYearlyStats } from '../../config/api'

const YearlyStats = () => {
  const [loading, setLoading] = useState(true)
  const [yearlyStats, setYearlyStats] = useState(null)
  const [topProducts, setTopProducts] = useState([])
  const [quarterlyData, setQuarterlyData] = useState({})
  const [error, setError] = useState(null);
  const fetchYearlyStats = async () => {
      setLoading(true)
      try {
        const data = await getYearlyStats()
        setYearlyStats(data)
        setTopProducts(data.topSellingProducts)
        setQuarterlyData(data.quarterlyRevenue)
        setError(null)
      } catch (err) {
        console.error('Error fetching weekly stats:', err)
        setError('Failed to load weekly statistics: ' + (err.message || 'Unknown error'))
      } finally {
        setLoading(false)
      }
    }

      useEffect(() => {
        fetchYearlyStats()
        
        // Set up an interval to refresh data every 5 minutes
        const interval = setInterval(() => {
          fetchYearlyStats()
        }, 300000)
        
        // Clean up the interval when component unmounts
        return () => clearInterval(interval)
      }, [])
    // Fetch yearly stats from backend API
    

  // Calculate total quarterly revenue for progress calculations
  const calculateTotalQuarterlyRevenue = () => {
    if (!quarterlyData || Object.keys(quarterlyData).length === 0) return 0
    return Object.values(quarterlyData).reduce((sum, value) => sum + value, 0)
  }

  const totalQuarterlyRevenue = calculateTotalQuarterlyRevenue()

  // Helper function to format currency
  const formatCurrency = (value) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'INR',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0
    }).format(value)
  }

  // Show loading spinner while data is being fetched
  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', p: 5 }}>
        <CircularProgress />
      </Box>
    )
  }
  console.log("yearly stats:",topProducts);
  
  return (
    
    <Card>
      <CardHeader
        title={`Yearly Performance ${yearlyStats?.year || new Date().getFullYear()}`}
        titleTypographyProps={{ sx: { lineHeight: '1.6 !important', letterSpacing: '0.15px !important' } }}
        
      />
      <CardContent sx={{ pt: theme => `${theme.spacing(1)} !important` }}>
        <Box sx={{ mb: 1, display: 'flex', alignItems: 'center' }}>
          <Typography variant='h4' sx={{ fontWeight: 600, fontSize: '2.125rem !important' }}>
          ₹{yearlyStats ? Math.round(yearlyStats.yearlyRevenue) : '$0'}
          </Typography>
          <Box sx={{ 
            display: 'flex', 
            alignItems: 'center', 
            color: yearlyStats && yearlyStats.revenueTrend >= 0 ? 'success.main' : 'error.main' 
          }}>
            {yearlyStats && yearlyStats.revenueTrend >= 0 ? (
              <MenuUp sx={{ fontSize: '1.875rem', verticalAlign: 'middle' }} />
            ) : (
              <MenuDown sx={{ fontSize: '1.875rem', verticalAlign: 'middle' }} />
            )}
            <Typography variant='body2' sx={{ 
              fontWeight: 600, 
              color: yearlyStats && yearlyStats.revenueTrend >= 0 ? 'success.main' : 'error.main' 
            }}>
              {yearlyStats ? `${Math.abs(yearlyStats.revenueTrend).toFixed(1)}%` : '0%'}
            </Typography>
          </Box>
        </Box>

        <Typography component='p' variant='caption' sx={{ mb: 3 }}>
          {yearlyStats && yearlyStats.revenueTrend >= 0 
            ? `Growth compared to previous year` 
            : `Decline compared to previous year`}
        </Typography>

        <Grid container spacing={4} sx={{ mb: 4 }}>
          <Grid item xs={12} sm={4}>
            <Box sx={{ textAlign: 'center', p: 2 }}>
              <Avatar
                sx={{ 
                  mb: 2, 
                  width: 50,
                  height: 50,
                  margin: '0 auto',
                  color: 'white',
                  backgroundColor: 'primary.main'
                }}
              >
                <CartOutline />
              </Avatar>
              <Typography variant='h6'>{yearlyStats?.yearlyOrdersCount || 0}</Typography>
              <Typography variant='body2'>Total Orders</Typography>
              <Chip 
                size='small' 
                label={`${Math.abs(yearlyStats?.ordersTrend || 0).toFixed(1)}%`}
                color={yearlyStats && yearlyStats.ordersTrend >= 0 ? 'success' : 'error'}
                sx={{ mt: 1 }}
              />
            </Box>
          </Grid>
          <Grid item xs={12} sm={4}>
            <Box sx={{ textAlign: 'center', p: 2 }}>
              <Avatar
                sx={{ 
                  mb: 2, 
                  width: 50,
                  height: 50,
                  margin: '0 auto',
                  color: 'white',
                  backgroundColor: 'info.main'
                }}
              >
                <AccountOutline />
              </Avatar>
              <Typography variant='h6'>{yearlyStats?.newCustomersCount || 0}</Typography>
              <Typography variant='body2'>New Customers</Typography>
              <Chip 
                size='small' 
                label={`${Math.abs(yearlyStats?.newCustomersTrend || 0).toFixed(1)}%`}
                color={yearlyStats && yearlyStats.newCustomersTrend >= 0 ? 'success' : 'error'}
                sx={{ mt: 1 }}
              />
            </Box>
          </Grid>
          <Grid item xs={12} sm={4}>
            <Box sx={{ textAlign: 'center', p: 2 }}>
              <Avatar
                sx={{ 
                  mb: 2, 
                  width: 50,
                  height: 50,
                  margin: '0 auto',
                  color: 'white',
                  backgroundColor: 'success.main'
                }}
              >
                {/* <CurrencyUsd /> */}
              </Avatar>
              <Typography variant='h6'>₹{Math.round(yearlyStats?.averageOrderValue || 0) }</Typography>
              <Typography variant='body2'>Avg. Order Value</Typography>
              <Chip 
                size='small' 
                label={`${Math.abs(yearlyStats?.avgOrderValueTrend || 0).toFixed(1)}%`}
                color={yearlyStats && yearlyStats.avgOrderValueTrend >= 0 ? 'success' : 'error'}
                sx={{ mt: 1 }}
              />
            </Box>
          </Grid>
        </Grid>

        <Divider sx={{ mb: 4 }} />

        <Typography variant='h6' sx={{ mb: 3 }}>
          Top Selling Products
        </Typography>

        {topProducts.map((item, index) => {
          // Calculate progress percentage based on revenue contribution to top products
          const totalTopRevenue = topProducts.reduce((sum, prod) => sum + prod.revenue, 0)
          const progress = totalTopRevenue > 0 ? (item.revenue / totalTopRevenue) * 100 : 0
          
          return (
            <Box
              key={item.id}
              sx={{
                display: 'flex',
                alignItems: 'center',
                ...(index !== topProducts.length - 1 ? { mb: 4 } : {})
              }}
            >
              <Avatar
  variant='rounded'
  src={item.image}
  alt={item.title}
  sx={{
    mr: 3,
    width: 40,
    height: 40
  }}
/>

              <Box
                sx={{
                  width: '100%',
                  display: 'flex',
                  flexWrap: 'wrap',
                  alignItems: 'center',
                  justifyContent: 'space-between'
                }}
              >
                <Box sx={{ marginRight: 2, display: 'flex', flexDirection: 'column' }}>
                  <Typography variant='body2' sx={{ mb: 0.5, fontWeight: 600, color: 'text.primary' }}>
                    {item.title}
                  </Typography>
                  <Typography variant='caption'>{item.category}</Typography>
                </Box>

                <Box sx={{ minWidth: 85, display: 'flex', flexDirection: 'column' }}>
                  <Typography variant='body2' sx={{ mb: 0.5, fontWeight: 600, color: 'text.primary' }}>
                    {formatCurrency(item.revenue)}
                  </Typography>
                  <Typography variant='caption'>{`${item.quantitySold} sold`}</Typography>
                  <LinearProgress 
                    value={progress} 
                    variant='determinate' 
                    sx={{ height: 6, mt: 1 }} 
                    color={index === 0 ? 'primary' : index === 1 ? 'info' : 'secondary'} 
                  />
                </Box>
              </Box>
            </Box>
          )
        })}

        
          

        {/* <Box sx={{ mt: 6, display: 'flex', alignItems: 'center' }}>
          <TrendingUp sx={{ mr: 1.5, fontSize: '1.75rem', color: 'primary.main' }} />
          <Typography variant='body2'>
            Projected growth for next year: <strong>{yearlyStats?.growthProjection.toFixed(1)}%</strong>
          </Typography>
        </Box> */}
      </CardContent>
    </Card>
  )
}

export default YearlyStats