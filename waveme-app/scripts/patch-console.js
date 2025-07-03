#!/usr/bin/env node

// Global patch for shadow* deprecation warnings from expo-router
// This must be loaded before any other modules

const originalWarn = console.warn;
console.warn = (...args) => {
  const message = args[0];
  if (typeof message === 'string' && message.includes('"shadow*" style props are deprecated. Use "boxShadow"')) {
    return; // Filter out expo-router shadow warnings
  }
  originalWarn.apply(console, args);
};

// Re-export original process behavior
module.exports = process;
