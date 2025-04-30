// ** MUI Imports
import Box from '@mui/material/Box'
import Grid from '@mui/material/Grid'
import Card from '@mui/material/Card'
import Avatar from '@mui/material/Avatar'
import CardHeader from '@mui/material/CardHeader'
import IconButton from '@mui/material/IconButton'
import Typography from '@mui/material/Typography'
import CardContent from '@mui/material/CardContent'
import { useEffect, useState } from 'react'
import CircularProgress from '@mui/material/CircularProgress'

// ** Icons Imports
import TrendingUp from 'mdi-material-ui/TrendingUp'
import CurrencyRupeeIcon from '@mui/icons-material/CurrencyRupee';
import DotsVertical from 'mdi-material-ui/DotsVertical'
import CellphoneLink from 'mdi-material-ui/CellphoneLink'
import AccountOutline from 'mdi-material-ui/AccountOutline'
import RefreshIcon from 'mdi-material-ui/Refresh'

// API service
import { getDashboardStats } from '../../config/api'

const MonthlyOverview = () => {
  const [statsData, setStatsData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  const fetchDashboardStats = async () => {
    setLoading(true)
    try {
      const data = await getDashboardStats()
      // Validate that the response is what we expect
      if (data && typeof data === 'object' && !Array.isArray(data)) {
        setStatsData(data)
        setError(null)
      } else {
        throw new Error('Invalid data received from server')
      }
    } catch (err) {
      console.error('Error fetching dashboard stats:', err)
      
      // Check for authentication error
      if (err.message && err.message.includes('Authentication required')) {
        setError('Authentication required. Please log in again.')
        // Optionally redirect to login page
        // window.location.href = '/login'
      } else {
        setError('Failed to load dashboard statistics: ' + (err.message || 'Unknown error'))
      }
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchDashboardStats()
    
    // Set up an interval to refresh data every 60 seconds
    const interval = setInterval(() => {
      fetchDashboardStats()
    }, 60000)
    
    // Clean up the interval when component unmounts
    return () => clearInterval(interval)
  }, [])

  const renderStats = () => {
    if (loading && !statsData) {
      return (
        <Grid item xs={12} sx={{ display: 'flex', justifyContent: 'center', py: 5 }}>
          <CircularProgress />
        </Grid>
      )
    }

    if (error) {
      return (
        <Grid item xs={12} sx={{ textAlign: 'center', py: 5 }}>
          <Typography color="error">{error}</Typography>
          <Box sx={{ mt: 2 }}>
            <IconButton color="primary" onClick={fetchDashboardStats}>
              <RefreshIcon />
            </IconButton>
          </Box>
        </Grid>
      )
    }

    const salesData = [
      {
        stats: statsData && statsData.sales !== undefined ? `${statsData.sales}` : '0',
        title: 'Sales',
        color: 'primary',
        icon: <TrendingUp sx={{ fontSize: '1.75rem' }} />
      },
      {
        stats: statsData && statsData.customers !== undefined ? `${statsData.customers}` : '0',
        title: 'Customers',
        color: 'success',
        icon: <AccountOutline sx={{ fontSize: '1.75rem' }} />
      },
      {
        stats: statsData && statsData.products !== undefined ? `${statsData.products}` : '0',
        color: 'warning',
        title: 'Products',
        icon: <CellphoneLink sx={{ fontSize: '1.75rem' }} />
      },
      {
        stats: statsData && statsData.revenue !== undefined ? `${'₹' + statsData.revenue.toFixed(2)}` : '₹0',
        color: 'info',
        title: 'Revenue',
        icon: <CurrencyRupeeIcon sx={{ fontSize: '1.75rem' }} />
      }
    ]

    return salesData.map((item, index) => (
      <Grid item xs={12} sm={3} key={index}>
        <Box key={index} sx={{ display: 'flex', alignItems: 'center' }}>
          <Avatar
            variant='rounded'
            sx={{
              mr: 3,
              width: 44,
              height: 44,
              boxShadow: 3,
              color: 'common.white',
              backgroundColor: theme => theme.palette[item.color].main
            }}
          >
            {item.icon}
          </Avatar>
          <Box sx={{ display: 'flex', flexDirection: 'column' }}>
            <Typography variant='caption'>{item.title}</Typography>
            <Typography variant='h6'>{item.stats}</Typography>
          </Box>
        </Box>
      </Grid>
    ))
  }

  return (
    <Card>
      <CardHeader
        title='Monthly Overview'
        action={
          <Box>
            <IconButton 
              size='small' 
              aria-label='refresh'
              onClick={fetchDashboardStats}
              sx={{ mr: 1, color: 'primary.main' }}
            >
              <RefreshIcon />
            </IconButton>
            <IconButton 
              size='small' 
              aria-label='settings' 
              className='card-more-options' 
              sx={{ color: 'text.secondary' }}
            >
              <DotsVertical />
            </IconButton>
          </Box>
        }
        subheader={
          <Typography variant='body2'>
            <Box component='span' sx={{ fontWeight: 600, color: 'text.primary' }}>
              Total {statsData && statsData.growthRate !== undefined ? `${statsData.growthRate.toFixed(2)}%` : '0%'} growth
            </Box>{' '}
            😎 this month
          </Typography>
        }
        titleTypographyProps={{
          sx: {
            mb: 2.5,
            lineHeight: '2rem !important',
            letterSpacing: '0.15px !important'
          }
        }}
      />
      <CardContent sx={{ pt: theme => `${theme.spacing(3)} !important` }}>
        <Grid container spacing={[5, 0]}>
          {renderStats()}
        </Grid>
      </CardContent>
    </Card>
  )
}

export default MonthlyOverview