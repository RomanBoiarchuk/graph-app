import {gql} from "@apollo/client";

export const GRAPH_SUBSCRIPTION = gql`
    subscription GraphSubscription {
        graph {
            vertexes {
                id
                name
            }
            edges {
                id
                weight
                from
                to
            }
        }
    }
`
