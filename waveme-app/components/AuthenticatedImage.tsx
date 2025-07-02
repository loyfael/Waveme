import React, { useState, useEffect } from 'react';
import { Image, ActivityIndicator, View, Text, StyleSheet } from 'react-native';
import axios from 'axios';
import { BASE_SERVER_URL } from '@/constants/API';

type AuthenticatedImageProps = {
  imageUrl: string;
  style?: any;
  resizeMode?: 'cover' | 'contain' | 'stretch' | 'repeat' | 'center';
  onError?: () => void;
  onLoad?: () => void;
};

export default function AuthenticatedImage({ imageUrl, style, resizeMode = 'cover', onError, onLoad }: AuthenticatedImageProps) {
  const [imageUri, setImageUri] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(false);

  useEffect(() => {
    const loadImage = async () => {
      try {
        setIsLoading(true);
        setError(false);

        // Build complete URL
        const fullUrl = imageUrl.startsWith('http') ? imageUrl : `${BASE_SERVER_URL}${imageUrl}`;
        
        // Fetch image with authentication headers
        const response = await axios.get(fullUrl, {
          responseType: 'arraybuffer',
          headers: {
            'Authorization': axios.defaults.headers.common['Authorization'] || '',
          },
        });

        // Convert to blob and create object URL
        const blob = new Blob([response.data]);
        const objectUrl = URL.createObjectURL(blob);
        setImageUri(objectUrl);
        onLoad?.();
      } catch (err) {
        console.error('Failed to load authenticated image:', err);
        setError(true);
        onError?.();
      } finally {
        setIsLoading(false);
      }
    };

    loadImage();

    // Cleanup function to revoke object URL
    return () => {
      if (imageUri) {
        URL.revokeObjectURL(imageUri);
      }
    };
  }, [imageUrl]);

  if (isLoading) {
    return (
      <View style={[style, styles.loadingContainer]}>
        <ActivityIndicator size="large" />
      </View>
    );
  }

  if (error) {
    return (
      <View style={[style, styles.errorContainer]}>
        <Text style={styles.errorText}>Erreur de chargement</Text>
      </View>
    );
  }

  return imageUri ? (
    <Image 
      source={{ uri: imageUri }} 
      style={[
        style, 
        resizeMode === 'cover' && { objectFit: 'cover' },
        resizeMode === 'contain' && { objectFit: 'contain' },
        resizeMode === 'stretch' && { objectFit: 'fill' },
      ]}
      onError={() => {
        setError(true);
        onError?.();
      }}
    />
  ) : null;
}

const styles = StyleSheet.create({
  loadingContainer: {
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f0f0f0',
  },
  errorContainer: {
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f8f8f8',
  },
  errorText: {
    color: '#666',
    fontSize: 14,
  },
});
