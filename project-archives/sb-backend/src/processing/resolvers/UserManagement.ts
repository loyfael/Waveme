import { db } from "../../database/PrismaClient";
import { AuthPayload, MutationResolvers } from "../../generated/graphql";
import MailService from "../../libs/mail/MailService";
import UserSecurity from "../../libs/user/UserSecurity";
import UserUtils from "../../libs/user/UserUtils";
const jwt = require('jsonwebtoken');
require('dotenv').config();

const userUtils = new UserUtils;
const userSecurity = new UserSecurity;
const mailService = new MailService;
const key = process.env.JWT_SECRET_KEY as string;

export const UserManagement: MutationResolvers = {
    createUser: async(_, args): Promise<boolean> => {
        const userName = args.userInput.userName;
        const email = args.userInput.email;
        const rawPassword = args.userInput.password;

        try {
            userUtils.userCredentialsAlreadyTaken(email, userName);
            const password = await userSecurity.hashPassword(rawPassword);

            await db.user.create(
                {
                    data: {
                        userName: userName,
                        email: email,
                        password: password
                    }
                }
            )

            mailService.userCreatedNewAccount(userName, email);

            return true;
        } catch (error) {
            console.error(`[SERVER] Something wrong: ${error}`);
            return false;
        }
    },
    
    userLogin: async(_, args): Promise<AuthPayload> => {
        const email = args.email;
        const password = args.password;
        const userName = await userUtils.getUserName(email);
        const userId = await userUtils.getUserId(email);

        try {
            userUtils.userExist(email, userName)

            if(userSecurity.verifyPassword(password, userName, email)) {
                const token = jwt.sign(
                    {
                        userId: userId,
                    },
                    key,
                    {
                        expireIn: "1h",
                    }
                );

                const response: AuthPayload = {
                    success: true,
                    token,
                    user: {
                        userId: userId as number
                    },
                }

                return response
            }
        } catch(error) {
            console.error("[SERVER] Something wrong at login: ", error)
            const response: AuthPayload = {
                success: false,
                token: null,
                user: {
                    userId: null
                },
            }

            return response
        }
    }
}