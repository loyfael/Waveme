import { Resolvers } from "../generated/graphql";
import { UserManagement } from "./resolvers/UserManagement";

/**
 * ResolverLinker
 * 
 * Permit to enable resolvers. If you create a resolver and you don't
 * enable there, it can't work.
 */
export const resolverLinker: Resolvers = {
    // Query: {
        
    // },

    Mutation: {
        createUser: UserManagement.createUser,
    }
}