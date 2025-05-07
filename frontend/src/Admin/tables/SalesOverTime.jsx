import { useState, useEffect } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { Settings } from 'lucide-react';

const SalesOverTime = () => {
  const [loading, setLoading] = useState(true);
  const [salesData, setSalesData] = useState([]);
  const [growthRate, setGrowthRate] = useState(0);
  const [viewMode, setViewMode] = useState('weekly'); // 'weekly', 'quarterly', 'yearly'
  const [yAxisDomain, setYAxisDomain] = useState([0, 'auto']);

  useEffect(() => {
    fetchData();
  }, [viewMode]);

  const fetchData = async () => {
    setLoading(true);
    try {
      let response;
      
      if (viewMode === 'weekly') {
        response = await fetch('/api/stats/weekly');
        const data = await response.json();
        
        // Create last 7 days labels
        const labels = [];
        for (let i = 6; i >= 0; i--) {
          const date = new Date();
          date.setDate(date.getDate() - i);
          labels.push(date.toLocaleDateString('en-US', { weekday: 'short' }));
        }
        
        // Mock daily data based on weekly stats
        const weeklyProfit = data.weeklyProfit;
        const dailyAverage = weeklyProfit / 7;
        const chartData = labels.map((day) => ({
          name: day,
          sales: dailyAverage * (0.7 + Math.random() * 0.6),
          highlight: false
        }));
        
        // Highlight the highest value
        const maxIndex = chartData.reduce((maxIdx, item, idx, arr) => 
          item.sales > arr[maxIdx].sales ? idx : maxIdx, 0);
        chartData[maxIndex].highlight = true;
        
        setSalesData(chartData);
        setGrowthRate(data.profitTrend);
        
        // Set y-axis domain based on data range
        const maxValue = Math.max(...chartData.map(item => item.sales));
        setYAxisDomain([0, Math.ceil(maxValue * 1.2)]); // Add 20% padding on top
      } else if (viewMode === 'quarterly') {
        response = await fetch('/api/stats/yearly');
        const data = await response.json();
        
        const quarterlyLabels = ['Q1', 'Q2', 'Q3', 'Q4'];
        const quarterlyValues = Object.values(data.quarterlyRevenue || {});
        
        const quarterlyData = quarterlyLabels.map((quarter, index) => ({
          name: quarter,
          sales: quarterlyValues[index] || 0,
          highlight: index === quarterlyValues.indexOf(Math.max(...quarterlyValues))
        }));
        
        setSalesData(quarterlyData);
        setGrowthRate(data.revenueTrend);
        
        // Set y-axis domain based on data range
        const maxValue = Math.max(...quarterlyData.map(item => item.sales));
        setYAxisDomain([0, Math.ceil(maxValue * 1.2)]); // Add 20% padding on top
      } else {
        // Yearly view - showing last 5 years (with mocked past data)
        response = await fetch('/api/stats/yearly');
        const data = await response.json();
        
        const currentYear = new Date().getFullYear();
        const yearLabels = [
          (currentYear-4).toString(), 
          (currentYear-3).toString(), 
          (currentYear-2).toString(), 
          (currentYear-1).toString(), 
          currentYear.toString()
        ];
        
        // Create mocked yearly data with a growth trend leading to current year
        const yearlyRevenue = data.yearlyRevenue;
        const growthProjection = data.growthProjection / 100;
        
        // Work backwards from current year data
        let currentYearData = yearlyRevenue;
        const yearlyChartData = yearLabels.map((year, index) => {
          // Each past year has less revenue based on growth projection
          const yearValue = index === yearLabels.length - 1 
            ? currentYearData 
            : currentYearData / (1 + (growthProjection * (yearLabels.length - 1 - index) / 5));
          
          return {
            name: year,
            sales: yearValue,
            highlight: index === yearLabels.length - 1 // Highlight current year
          };
        });
        
        setSalesData(yearlyChartData);
        setGrowthRate(data.growthProjection);
        
        // Set y-axis domain based on data range
        const maxValue = Math.max(...yearlyChartData.map(item => item.sales));
        setYAxisDomain([0, Math.ceil(maxValue * 1.2)]); // Add 20% padding on top
      }
    } catch (error) {
      console.error('Error fetching sales data:', error);
      // Set fallback data
      const fallbackData = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'].map((day, index) => ({
        name: day,
        sales: [37, 57, 45, 75, 57, 40, 65][index],
        highlight: index === 3
      }));
      setSalesData(fallbackData);
      setGrowthRate(45);
      setYAxisDomain([0, 100]);
    } finally {
      setLoading(false);
    }
  };

  const toggleViewMode = () => {
    if (viewMode === 'weekly') {
      setViewMode('quarterly');
    } else if (viewMode === 'quarterly') {
      setViewMode('yearly');
    } else {
      setViewMode('weekly');
    }
  };

  const formatCurrency = (value) => {
    if (value >= 1000000) {
      return `$${(value / 1000000).toFixed(1)}M`;
    } else if (value >= 1000) {
      return `$${(value / 1000).toFixed(1)}k`;
    } else {
      return `$${value.toFixed(0)}`;
    }
  };

  // Custom Bar component to handle dynamic colors
  const renderCustomBar = (props) => {
    const { x, y, width, height, index } = props;
    const isHighlighted = salesData[index]?.highlight;
    const fill = isHighlighted ? '#3b82f6' : '#4b5563'; // Blue for highlighted, gray for others
    
    return <rect x={x} y={y} width={width} height={height} fill={fill} radius={[9, 9, 0, 0]} rx={9} ry={9} />;
  };

  return (
    <div className="bg-gray-900 rounded-lg shadow-xl overflow-hidden border border-gray-800">
      {/* Card Header */}
      <div className="flex justify-between items-center p-4 border-b border-gray-700">
        <h3 className="text-lg font-medium text-gray-200">
          Sales {viewMode === 'weekly' ? 'This Week' : viewMode === 'quarterly' ? 'By Quarter' : 'Year Over Year'}
        </h3>
        <button className="p-1 text-gray-400 hover:text-gray-200 focus:outline-none">
          <Settings size={20} />
        </button>
      </div>

      {/* Card Content */}
      <div className="p-4">
        {loading ? (
          <div className="flex justify-center items-center h-64">
            <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
          </div>
        ) : (
          <div className="h-64">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart
                data={salesData}
                margin={{ top: 10, right: 10, left: 0, bottom: 10 }}
              >
                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#374151" />
                <XAxis 
                  dataKey="name" 
                  tick={{ fill: '#9ca3af' }}
                  axisLine={{ stroke: '#4b5563' }}
                  tickLine={{ stroke: '#4b5563' }}
                />
                <YAxis 
                  tickFormatter={formatCurrency}
                  tick={{ fill: '#9ca3af' }}
                  axisLine={{ stroke: '#4b5563' }}
                  tickLine={{ stroke: '#4b5563' }}
                  width={60}
                  domain={yAxisDomain}
                />
                <Tooltip 
                  formatter={(value) => [formatCurrency(value), 'Sales']}
                  labelStyle={{ color: '#e5e7eb' }}
                  contentStyle={{ 
                    backgroundColor: '#1f2937', 
                    border: '1px solid #374151',
                    borderRadius: '4px',
                    color: '#e5e7eb'
                  }}
                />
                <Bar 
                  dataKey="sales" 
                  shape={renderCustomBar}
                />
              </BarChart>
            </ResponsiveContainer>
          </div>
        )}

        {/* Growth Rate Info */}
        <div className="flex items-center mt-4 mb-6">
          <span className={`text-xl font-bold mr-3 ${growthRate >= 0 ? 'text-green-500' : 'text-red-500'}`}>
            {growthRate.toFixed(1)}%
          </span>
          <p className="text-gray-300 text-sm">
            Your sales performance is {Math.abs(growthRate).toFixed(1)}% 
            {growthRate >= 0 ? ' better 😎' : ' worse 😔'} compared to 
            {viewMode === 'weekly' ? ' last week' : 
            viewMode === 'quarterly' ? ' last quarter' : ' last year'}
          </p>
        </div>

        {/* Toggle Button */}
        <button 
          className="w-full py-2 px-4 bg-blue-600 text-white font-medium rounded hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50"
          onClick={toggleViewMode}
        >
          {viewMode === 'weekly' ? 'View Quarterly' : 
           viewMode === 'quarterly' ? 'View Yearly' : 'View Weekly'}
        </button>
      </div>
    </div>
  );
};

export default SalesOverTime;