import { Link } from 'expo-router';
import { openBrowserAsync } from 'expo-web-browser';
import React from 'react';
import { ComponentProps } from 'react';
import { Platform, Text, TouchableOpacity } from 'react-native';
import { isWebUri } from 'valid-url';

type Props = Omit<ComponentProps<typeof Link>, 'href'> & { href: string };

// Fonction utilitaire pour filtrer les props spécifiques à React Native
function filterNativeProps(props: any) {
  const {
    allowFontScaling,
    ellipsizeMode,
    lineBreakMode,
    numberOfLines,
    style,
    ...restProps
  } = props;
  return restProps;
}

export function ExternalLink({ href, ...rest }: Props) {
  const isExternal = isWebUri(href);

  if (Platform.OS === 'web' && isExternal) {
    // Filtrer les props spécifiques à React Native pour éviter les erreurs sur le web
    const webProps = filterNativeProps(rest);

    return (
      <a href={href} target="_blank" rel="noopener noreferrer" {...webProps}>
        <span style={{ color: 'blue', textDecoration: 'underline' }}>
          {rest.children}
        </span>
      </a>
    );
  }

  if (Platform.OS !== 'web' && isExternal) {
    return (
      <TouchableOpacity
        onPress={async () => {
          await openBrowserAsync(href);
        }}
        {...rest}
      >
        <Text style={{ color: 'blue', textDecorationLine: 'underline' }}>
          {rest.children}
        </Text>
      </TouchableOpacity>
    );
  }

  // Pour un lien interne, utiliser le composant Link d'expo-router
  return <Link href={href as any} {...rest} />;
}
