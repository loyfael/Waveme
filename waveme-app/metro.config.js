const { getDefaultConfig } = require('expo/metro-config');

// Suppress shadow* deprecation warnings
const originalWarn = console.warn;
console.warn = (...args) => {
  const message = args[0];
  if (typeof message === 'string' && message.includes('"shadow*" style props are deprecated. Use "boxShadow"')) {
    return;
  }
  originalWarn.apply(console, args);
};

const config = getDefaultConfig(__dirname);

module.exports = config;
