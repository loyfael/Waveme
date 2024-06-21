import bcrypt from 'bcrypt';
import { db } from '../../database/PrismaClient';
require('dotenv').config();

const SALT_ROUND = process.env.SALT_ROUND;

export default class UserSecurity {
    async hashPassword(password: string) {
        try {
            const salt = await bcrypt.genSalt(SALT_ROUND);

            const hashedPassword = await bcrypt.hash(password, salt);
            return hashedPassword;
        } catch (error) {
            console.error('Erreur lors du hachage du mot de passe:', error);
            return null;
        }
    }

    async verifyPassword(plainPassword: string, userName: string, email: string): Promise<boolean> {
        try {
            const user = await db.user.findUnique({
                where: {
                    userName: userName,
                    email: email,
                },
                select: {
                    userName: true,
                    email: true,
                    password: true,
                }
            })

            const isMatch = await bcrypt.compareSync(plainPassword, user.password);

            return isMatch;
        } catch (error) {
            console.error('Erreur lors de la vérification du mot de passe:', error);
            return false;
        }
    }
}