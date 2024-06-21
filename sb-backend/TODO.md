
Configurer l'authentification du côté d'un Apollo Server pour une application utilisant GraphQL nécessite de mettre en place quelques éléments clés pour gérer les jetons d'authentification et les vérifications de sécurité des utilisateurs. Voici les étapes essentielles pour configurer l'authentification sur Apollo Server :

1. Installation des dépendances
Avant de commencer, assurez-vous que votre projet Node.js a Apollo Server et les autres dépendances nécessaires installées. Pour l'authentification, vous aurez également besoin d'une bibliothèque pour gérer les jetons JWT, comme jsonwebtoken.

```bash
npm install apollo-server express jsonwebtoken
```
2. Création d'un schéma GraphQL avec Authentification
Définissez des types dans votre schéma GraphQL pour l'authentification. Par exemple, vous pouvez avoir une mutation login qui retourne un AuthPayload.

```graphql
type Query {
  currentUser: User
}

type Mutation {
  login(email: String!, password: String!): AuthPayload
}

type User {
  id: ID!
  email: String!
  name: String
}

type AuthPayload {
  token: String
  user: User
}
```
3. Résolveurs pour l'Authentification
Créez des résolveurs pour la mutation login. Vous vérifierez les identifiants de l'utilisateur, générerez un jeton si les identifiants sont valides, et retournerez ce jeton avec les détails de l'utilisateur.
```ts
const jwt = require('jsonwebtoken');
const { ApolloServer, gql } = require('apollo-server');

const typeDefs = `...`; // Votre schéma GraphQL

const resolvers = {
  Mutation: {
    login: async (_, { email, password }) => {
      // Ici, vous vérifieriez les identifiants dans votre base de données
      const user = await db.findUserByEmail(email);
      
      if (user && bcrypt.compareSync(password, user.password)) {
        const token = jwt.sign({ userId: user.id }, 'your_secret_key', { expiresIn: '1h' });
        return { token, user };
      }
      throw new Error('Invalid login');
    }
  },
  Query: {
    currentUser: async (_, args, { user }) => {
      return user ? db.findUserById(user.userId) : null;
    }
  }
};

const server = new ApolloServer({
  typeDefs,
  resolvers,
  context: ({ req }) => {
    const token = req.headers.authorization || '';
    if (token) {
      try {
        const user = jwt.verify(token, 'your_secret_key');
        return { user };
      } catch (e) {
        throw new Error('Your session expired. Sign in again.');
      }
    }
  }
});

server.listen().then(({ url }) => {
  console.log(`🚀 Server ready at ${url}`);
});
```

4. Configuration du Contexte pour la Vérification des Jetons
Dans la configuration de ApolloServer, le contexte est une fonction qui est exécutée pour chaque requête. Vous pouvez l'utiliser pour lire le jeton JWT de l'en-tête Authorization, le décoder, et attacher l'utilisateur au contexte si le jeton est valide.

5. Sécuriser les Résolveurs
Utilisez le contexte pour sécuriser les résolveurs qui nécessitent une authentification. Par exemple, pour le résolveur currentUser, vérifiez si un utilisateur est attaché au contexte et retournez les informations de cet utilisateur.

6. Gestion des Erreurs
Gérez les erreurs liées à l'authentification, comme les jetons expirés ou invalides, en retournant des erreurs appropriées aux clients.

Ces étapes vous permettent de configurer un système d'authentification robuste dans Apollo Server. Gardez à l'esprit la nécessité de sécuriser les communications en utilisant HTTPS et de gérer les secrets d'authentification de manière sécurisée.
