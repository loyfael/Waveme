import { prisma } from "../database/PrismaClient";
require('dotenv').config();

export default class PlayerUtils {

    /**
     * Function for verify if player doesn't exist. Please use this function only
     * in this case. if player doesnt exist => return true, else throw an error.
     * @param playerId 
     * @returns 
     */
    async playerDontExist(userId: string): Promise<boolean> {
        try {
            await prisma.user.findFirst(
                {
                    where: {
                        userId: userId
                    }
                }
            )
    
            return true;
        } catch(error) {
            throw new Error(`[SERVER] Player already exist ${error}`)
        }
    }

    /**
     * Function for verify if player exist. Please use this function only
     * in this case. if player exist => return true, else throw an error.
     * @param playerId or user
     * @returns 
     */
    async playerExist(playerId: string): Promise<boolean> {
        try {
            await prisma.user.findFirst(
                {
                    where: {
                        playerId: playerId
                    }
                }
            )
    
            return true;
        } catch (error) {
            throw new Error(`[SERVER] Player don't exist ${error}`)
        }
    }
}