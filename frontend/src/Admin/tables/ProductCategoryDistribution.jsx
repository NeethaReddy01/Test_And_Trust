import { useEffect, useState } from 'react'
import { useTheme } from '@mui/material/styles'
import Box from '@mui/material/Box'
import CircularProgress from '@mui/material/CircularProgress'
import Typography from '@mui/material/Typography'
import axios from 'axios';
import React, { useRef } from 'react';


// ** Chart Imports
import { PieChart, Pie, Cell, ResponsiveContainer, Legend, Tooltip } from 'recharts'

const ProductCategoryDistribution = () => {
  const theme = useTheme()
  const [loading, setLoading] = useState(true)
  const [chartData, setChartData] = useState([])

  useEffect(() => {
    // Fetch yearly stats from backend API
    const fetchYearlyStats = async () => {
      try {
        setLoading(true)
        const response = await axios.get('/api/stats/yearly')
        
        if (response.data && response.data.topSellingProducts) {
          const products = response.data.topSellingProducts
          
          // Group products by category and sum their revenue
          const categoryRevenue = products.reduce((acc, product) => {
            const category = product.category
            if (!acc[category]) {
              acc[category] = 0
            }
            acc[category] += product.revenue
            return acc
          }, {})
          
          // Transform to chart data format
          const formattedData = Object.entries(categoryRevenue).map(([name, value]) => ({
            name,
            value
          }))
          
          setChartData(formattedData)
        }
        
        setLoading(false)
      } catch (error) {
        console.error('Error fetching category distribution data:', error)
        setLoading(false)
        
        // Fallback data if API fails
        setChartData([
          { name: 'Skincare', value: 35000 },
          { name: 'Makeup', value: 24000 },
          { name: 'Haircare', value: 18000 },
          { name: 'Fragrance', value: 15000 },
          { name: 'Other', value: 8000 }
        ])
      }
    }

    fetchYearlyStats()
  }, [])

  // Colors for pie chart segments
  const COLORS = [
    theme.palette.primary.main,
    theme.palette.secondary.main,
    theme.palette.info.main,
    theme.palette.success.main,
    theme.palette.warning.main,
    theme.palette.error.main
  ]

  // Helper function to format currency for tooltip
  const formatCurrency = (value) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0
    }).format(value)
  }

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
          p: 5, 
          border: `1px solid ${theme.palette.divider}`,
          borderRadius: 1
        }}>
          <Typography variant='body2' sx={{ fontWeight: 600 }}>
            {data.name}
          </Typography>
          <Typography variant='caption' sx={{ display: 'block' }}>
          ₹{(data.value)}
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
                {entry.value} ({percentage}%)
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

  return (
    <Box sx={{ height: 500 }}>
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
    </Box>
  )
}

export default ProductCategoryDistribution