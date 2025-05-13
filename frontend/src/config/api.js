import axios from 'axios';

const LOCALHOST = 'http://localhost:8080';
export const API_BASE_URL = LOCALHOST;

// Create API instance with base URL
const api = axios.create({
  baseURL: API_BASE_URL,
});

// Function to get the JWT token from localStorage
const getAuthToken = () => localStorage.getItem('jwt');

// Update axios headers with token before each request
api.interceptors.request.use(
  (config) => {
    const token = getAuthToken();
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Get dashboard statistics
export const getDashboardStats = async () => {
  try {
    // Use the configured api instance that includes auth headers
    const response = await api.get('/api/stats/dashboard');
    console.log("Stats data received:", response.data);
    return response.data;
  } catch (error) {
    console.error('Error fetching dashboard stats:', error);
    
    // Check if it's a redirect to login (status 200 but HTML content)
    if (error.response && error.response.status === 200 && 
        typeof error.response.data === 'string' && 
        error.response.data.includes('Please sign in')) {
      throw new Error('Authentication required. Please log in again.');
    }
    
    throw error;
  }
};

export const getWeeklyStats = async () => {
  try {
    // Use the configured api instance that includes auth headers
    const response = await api.get('/api/stats/weekly');
    console.log("Weekly stats data received:", response.data);
    return response.data;
  } catch (error) {
    console.error('Error fetching weekly stats:', error);
    
    // Check if it's a redirect to login (status 200 but HTML content)
    if (error.response && error.response.status === 200 && 
        typeof error.response.data === 'string' && 
        error.response.data.includes('Please sign in')) {
      throw new Error('Authentication required. Please log in again.');
    }
    
    throw error;
  }
};
export const getYearlyStats = async () => {
  try {
    // Use the configured api instance that includes auth headers
    const response = await api.get('/api/stats/yearly');
    console.log("Yearly stats data received:", response.data);
    return response.data;
  } catch (error) {
    console.error('Error fetching weekly stats:', error);
    
    // Check if it's a redirect to login (status 200 but HTML content)
    if (error.response && error.response.status === 200 && 
        typeof error.response.data === 'string' && 
        error.response.data.includes('Please sign in')) {
      throw new Error('Authentication required. Please log in again.');
    }
    
    throw error;
  }
};

export default api;