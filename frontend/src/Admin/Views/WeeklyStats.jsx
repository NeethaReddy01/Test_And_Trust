import { useEffect, useState } from 'react'
import Grid from '@mui/material/Grid'
import CardStatsVertical from '../../Styles/CardStatsVertical'
import { Poll, CurrencyUsd, BriefcaseVariantOutline, HelpCircleOutline } from 'mdi-material-ui'
import { getWeeklyStats } from '../../config/api'
import CircularProgress from '@mui/material/CircularProgress'
import Box from '@mui/material/Box'
import Typography from '@mui/material/Typography'
import IconButton from '@mui/material/IconButton'
import RefreshIcon from 'mdi-material-ui/Refresh'

const WeeklyStats = () => {
  const [statsData, setStatsData] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  const fetchWeeklyStats = async () => {
    setLoading(true)
    try {
      const data = await getWeeklyStats()
      setStatsData(data)
      setError(null)
    } catch (err) {
      console.error('Error fetching weekly stats:', err)
      setError('Failed to load weekly statistics: ' + (err.message || 'Unknown error'))
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchWeeklyStats()
    
    // Set up an interval to refresh data every 5 minutes
    const interval = setInterval(() => {
      fetchWeeklyStats()
    }, 300000)
    
    // Clean up the interval when component unmounts
    return () => clearInterval(interval)
  }, [])

  if (loading && !statsData) {
    return (
      <Grid container spacing={2} justifyContent="center" alignItems="center" style={{ minHeight: '200px' }}>
        <CircularProgress />
      </Grid>
    )
  }

  if (error) {
    return (
      <Grid container spacing={2} justifyContent="center" alignItems="center" style={{ minHeight: '200px' }}>
        <Grid item xs={12} sx={{ textAlign: 'center' }}>
          <Typography color="error">{error}</Typography>
          <Box sx={{ mt: 2 }}>
            <IconButton color="primary" onClick={fetchWeeklyStats}>
              <RefreshIcon />
            </IconButton>
          </Box>
        </Grid>
      </Grid>
    )
  }

  // Format currency to include currency symbol and two decimal places
  const formatCurrency = (value) => {
    return `₹${parseFloat(value).toFixed(2)}`
  }

  // Format percentage trend with + or - sign
  const formatTrend = (value) => {
    const formattedValue = parseFloat(value).toFixed(0)
    return value >= 0 ? `+${formattedValue}%` : `${formattedValue}%`
  }

  return (
    <Grid container spacing={2}>
      <Grid item xs={6}>
        <CardStatsVertical
          stats={statsData ? formatCurrency(statsData.weeklyProfit) : '₹0'}
          icon={<Poll />}
          color="success"
          trendNumber={statsData ? formatTrend(statsData.profitTrend) : '+0%'}
          title="Total Profit"
          subtitle="Weekly Profit"
        />
      </Grid>
      {/* <Grid item xs={6}>
        <CardStatsVertical
          stats={statsData ? formatCurrency(statsData.refundAmount) : '₹0'}
          title="Refunds"
          trend={statsData && statsData.refundTrend < 0 ? 'negative' : 'positive'}
          color="secondary"
          trendNumber={statsData ? formatTrend(statsData.refundTrend) : '0%'}
          subtitle="Past Month"
          icon={<CurrencyUsd />}
        />
      </Grid> */}
      <Grid item xs={6}>
        <CardStatsVertical
          stats={statsData ? statsData.newOrdersCount : '0'}
          trend={statsData && statsData.ordersTrend < 0 ? 'negative' : 'positive'}
          trendNumber={statsData ? formatTrend(statsData.ordersTrend) : '0%'}
          title="New Orders"
          subtitle="Weekly Orders"
          icon={<BriefcaseVariantOutline />}
        />
      </Grid>
      {/* <Grid item xs={6}>
        <CardStatsVertical
          stats={statsData ? statsData.salesQueries : '0'}
          color="warning"
          trend={statsData && statsData.queriesTrend < 0 ? 'negative' : 'positive'}
          trendNumber={statsData ? formatTrend(statsData.queriesTrend) : '0%'}
          subtitle="Last Week"
          title="Sales Queries"
          icon={<HelpCircleOutline />}
        />
      </Grid> */}
    </Grid>
  )
}

export default WeeklyStats