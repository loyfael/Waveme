import path from 'path';
import { loadSchemaSync } from '@graphql-tools/load';
import { GraphQLFileLoader } from '@graphql-tools/graphql-file-loader';
import { db } from '../database/PrismaClient';
import { ApolloServer } from '@apollo/server';
import { resolverLinker } from '../processing/ResolverLinker';
const express = require('express');
require('dotenv').config();

const app = express();
const port = process.env.SERVER_PORT || 4000;

const typeDefs = loadSchemaSync(
  path.join(__dirname, "../../graphql/index.graphql"),
  {
    loaders: [new GraphQLFileLoader()],
  }
);

async function testDatabaseConnection() {
  try {
    await db.$connect()
    console.log('✅ Database connection successful')
  } catch (error) {
    console.error('❌ Database connection failed.', error)
  }
}

const server = new ApolloServer({
  typeDefs,
  resolvers: resolverLinker,
});

async function startServer() {
  await server.start();
}

export default async function BackendService() {
  await startServer();
  await testDatabaseConnection();

  app.listen(port, () => {
    console.log(`🚀 http://localhost:${port}/graphql`);
  });
}
