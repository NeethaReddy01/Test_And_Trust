import { useEffect, useState } from 'react'
import { useTheme } from '@mui/material/styles'
import Box from '@mui/material/Box'
import CircularProgress from '@mui/material/CircularProgress'
import Typography from '@mui/material/Typography'
import React from 'react'

// ** Chart Imports
import { PieChart, Pie, Cell, ResponsiveContainer, Legend, Tooltip } from 'recharts'
import api, { getYearlyStats } from '../../config/api'
import { Divider, LinearProgress } from '@mui/material'

const ProductCategoryDistribution = () => {
  const theme = useTheme()
  const [productsByCategory, setProductsByCategory] = useState([])
  const [loading, setLoading] = useState(true)
  const [quarterlyData, setQuarterlyData] = useState({})
    const [error, setError] = useState(null);
    const fetchYearlyStats = async () => {
        setLoading(true)
        try {
          const data = await getYearlyStats()
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
    const formatCurrency = (value) => {
      return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'INR',
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
      }).format(value)
    }
  
    const totalQuarterlyRevenue = calculateTotalQuarterlyRevenue()
  
  const categories = [
    "Haircare", 
    "Bath", 
    "Body", 
    "Hygiene", 
    "Fragrance", 
    "Makeup", 
    "Cleanser", 
    "Moisturizer", 
    "Serum", 
    "Sunscreen"
  ]
  
  useEffect(() => {
    async function fetchCategoryData(category) {
      try {
        const result = await api.get(`/api/products?category=${category.toLowerCase()}`);
        // Make sure data is always an array
        const data = Array.isArray(result.data) ? result.data : [];
        return { category, data };
      } catch (error) {
        console.error(`Error fetching ${category} products:`, error);
        return { category, data: [] };
      }
    }
    
    async function fetchAllCategories() {
      setLoading(true);
      try {
        const categoryPromises = categories.map(category => fetchCategoryData(category));
        const results = await Promise.all(categoryPromises);
        console.log("results:", results);
        setProductsByCategory(results);
      } catch (error) {
        console.error("Error fetching products:", error);
      } finally {
        setLoading(false);
      }
    }

    fetchAllCategories();
  }, []);

  // Transform productsByCategory for the pie chart
  const chartData = productsByCategory.map(category => ({
    name: category.category,
    value: category.data.length
  })).filter(item => item.value > 0); // Only include categories with products

  // Colors for pie chart segments
  const COLORS = [
    theme.palette.primary.main,
    theme.palette.secondary.main,
    theme.palette.info.main,
    theme.palette.success.main,
    theme.palette.warning.main,
    theme.palette.error.main,
    '#8884d8',
    '#82ca9d',
    '#ffc658',
    '#ff8042'
  ]

  // Calculate total value for percentage calculation
  const totalValue = chartData.reduce((sum, entry) => sum + entry.value, 0)

  // Custom tooltip
  const CustomTooltip = ({ active, payload }) => {
    if (active && payload && payload.length) {
      const data = payload[0]
      const percentage = ((data.value / totalValue) * 100).toFixed(1)
      
      return (
        <Box sx={{ 
          backgroundColor: theme.palette.background.paper, 
          p: 2, 
          border: `1px solid ${theme.palette.divider}`,
          borderRadius: 1
        }}>
          <Typography variant='body2' sx={{ fontWeight: 600 }}>
            {data.name}
          </Typography>
          <Typography variant='caption' sx={{ display: 'block' }}>
            {data.value} products
          </Typography>
          <Typography variant='caption' sx={{ display: 'block' }}>
            {percentage}% of total
          </Typography>
        </Box>
      )
    }
    return null
  }

  // Custom legend with percentages
  const renderCustomizedLegend = (props) => {
    const { payload } = props
    
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', mt: 2 }}>
        {payload.map((entry, index) => {
          const percentage = ((entry.payload.value / totalValue) * 100).toFixed(1)
          
          return (
            <Box 
              key={`item-${index}`} 
              sx={{ 
                display: 'flex', 
                alignItems: 'center', 
                mb: 1
              }}
            >
              <Box 
                sx={{ 
                  width: 12, 
                  height: 12, 
                  borderRadius: '50%', 
                  backgroundColor: entry.color, 
                  mr: 1 
                }} 
              />
              <Typography variant='caption' sx={{ fontSize: '0.75rem' }}>
                {entry.value} - {entry.name} ({percentage}%)
              </Typography>
            </Box>
          )
        })}
      </Box>
    )
  }

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', p: 5 }}>
        <CircularProgress />
      </Box>
    )
  }

  // Show message if no data
  if (chartData.length === 0) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: 300 }}>
        <Typography variant="body1">No product data available</Typography>
      </Box>
    )
  }

  return (
    <Box sx={{ height: 500 }}>
      <Typography variant="h6" sx={{ mb: 2, textAlign: 'center' }}>
        Product Distribution by Category
      </Typography>
      <ResponsiveContainer width="100%" height="100%">
        <PieChart>
          <Pie
            data={chartData}
            cx="50%"
            cy="50%"
            innerRadius={60}
            outerRadius={80}
            paddingAngle={2}
            dataKey="value"
            labelLine={false}
          >
            {chartData.map((entry, index) => (
              <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
            ))}
          </Pie>
          <Tooltip content={<CustomTooltip />} />
          <Legend content={renderCustomizedLegend} />
        </PieChart>
      </ResponsiveContainer>
              <Divider sx={{ my: 4 }} />
      
      <Typography variant='h6' sx={{ mb: 3 }}>
                Quarterly Revenue Breakdown
              </Typography>
      
              {Object.entries(quarterlyData).map(([quarter, revenue], index) => {
                const progress = totalQuarterlyRevenue > 0 ? (revenue / totalQuarterlyRevenue) * 100 : 0
                
                const quarterColors = {
                  'Q1': 'primary',
                  'Q2': 'info',
                  'Q3': 'warning',
                  'Q4': 'success'
                }
                
                return (
                  <Box
                    key={quarter}
                    sx={{
                      display: 'flex',
                      alignItems: 'center',
                      ...(index !== Object.keys(quarterlyData).length - 1 ? { mb: 3 } : {})
                    }}
                  >
                    <Box sx={{ width: '100%' }}>
                      <Box
                        sx={{
                          display: 'flex',
                          alignItems: 'center',
                          justifyContent: 'space-between',
                          mb: 1
                        }}
                      >
                        <Typography variant='body2' sx={{ fontWeight: 600 }}>
                          {quarter}
                        </Typography>
                        <Typography variant='body2' sx={{ fontWeight: 600 }}>
                          {formatCurrency(revenue)}
                        </Typography>
                      </Box>
                      <LinearProgress
                        value={progress} 
                        variant='determinate' 
                        sx={{ height: 8 }} 
                        color={quarterColors[quarter]} 
                      />
                    </Box>
                  </Box>
                )
              })}
    </Box>
    
  )
}

export default ProductCategoryDistribution