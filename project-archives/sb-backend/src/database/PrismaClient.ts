import { PrismaClient } from "@prisma/client";

/**
 * This file contains the Prisma client instance. 
 * This single instance is necessary, as Prisma suffers from 
 * the shortcomings of instantiating its client several times.
 * 
 * Do not modify this file under any circumstances.
 */
export const db = new PrismaClient;