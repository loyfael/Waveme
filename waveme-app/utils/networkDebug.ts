import { Platform } from 'react-native';
import { BASE_SERVER_URL } from '@/constants/API';

export const logNetworkInfo = () => {
  console.log('=== NETWORK DEBUG INFO ===');
  console.log('Platform:', Platform.OS);
  console.log('Base Server URL:', BASE_SERVER_URL);
  console.log('Timestamp:', new Date().toISOString());
  console.log('========================');
};

export const testConnectivity = async () => {
  try {
    console.log('Testing connectivity to:', BASE_SERVER_URL);
    
    const response = await fetch(`${BASE_SERVER_URL}/api/health`, {
      method: 'GET',
    });
    
    console.log('Connectivity test result:', response.status);
    
    // 401 means server is accessible but requires auth - this is actually good!
    // 404 would mean endpoint doesn't exist
    // Network errors would throw an exception
    return response.status === 200 || response.status === 401;
  } catch (error) {
    console.error('Connectivity test failed:', error);
    return false;
  }
};
