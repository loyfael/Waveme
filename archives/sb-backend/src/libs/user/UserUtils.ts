import { db } from "../../database/PrismaClient";
require('dotenv').config();

export default class UserUtils {

    /**
     * Function for verify if user exist.
     * Verify if email is already used somewhere.
     * Verify if userName is already used somewhere.
     * @param userId or user
     * @returns 
     */
    async userCredentialsAlreadyTaken(email: string, userName: string): Promise<boolean> {
        try {
            const user = await db.user.findUnique({
                where: {
                    email: email,
                    userName: userName,
                }
            })

            if(user.email) throw new Error(`Email already taked`);
            if(user.userName) throw new Error(`UserName already taked !`);

            return true
        } catch (error) {
            throw new Error(`[SERVER] ${error}`)
        }
    }

    async getUserName(email: string): Promise<string> {
        try {
            const user = await db.user.findUnique(
                {
                    where: {
                        email: email
                    },
                    select: {
                        userName: true
                    }
                }
            );

            return user.userName;
        } catch(error) {
            throw new Error(`User not found..`)
        }
    }

    async getUserId(email: string): Promise<number> {
        try {
            const user = await db.user.findUnique(
                {
                    where: {
                        email: email
                    },
                    select: {
                        id: true
                    }
                }
            );

            return user.id;
        } catch(error) {
            throw new Error(`User not found..`)
        }
    }

    async userExist(email: string, userName: string): Promise<boolean> {
        try {
            const user = await db.user.findUnique(
                {
                    where: {
                        email: email,
                        userName: userName,
                    }
                }
            )

            if (user) {
                return true;
            } else {
                throw new Error(`User don't exist !`)
            }
        } catch(error) {
            throw new Error(`${error}`)
        }
    }
}