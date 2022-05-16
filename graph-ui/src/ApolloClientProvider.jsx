import {ApolloClient, ApolloProvider, HttpLink, InMemoryCache, split} from '@apollo/client';
import {GraphQLWsLink} from '@apollo/client/link/subscriptions';
import {createClient} from 'graphql-ws';
import {getMainDefinition} from '@apollo/client/utilities';
import * as React from "react";

const API_URL = process.env.REACT_APP_VERTEX_REGISTRY_API_URL;
const WS_API_URL = process.env.REACT_APP_VERTEX_REGISTRY_WS_URL;

const httpLink = new HttpLink({
    uri: `${API_URL}/graphql`,
});

const wsLink = new GraphQLWsLink(
    createClient({
        url: `${WS_API_URL}/graphql`,
    })
);

const splitLink = split(
    ({ query }) => {
        const definition = getMainDefinition(query);
        return definition.kind === 'OperationDefinition' && definition.operation === 'subscription';
    },
    wsLink,
    httpLink
);

export const client = new ApolloClient({
    link: splitLink,
    cache: new InMemoryCache(),
});

const ApolloClientProvider = ({ children }) => {
    return <ApolloProvider client={client}>{children}</ApolloProvider>;
};

export default ApolloClientProvider;
