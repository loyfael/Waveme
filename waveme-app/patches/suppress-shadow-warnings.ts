// Patch to suppress shadow* deprecation warnings from dependencies
// This can be removed when expo-router and other dependencies are updated

const originalWarn = console.warn;

console.warn = (...args) => {
  // Filter out shadow* deprecation warnings
  const message = args[0];
  if (typeof message === 'string' && message.includes('"shadow*" style props are deprecated. Use "boxShadow"')) {
    return;
  }
  
  // Allow all other warnings through
  originalWarn.apply(console, args);
};

export {};
