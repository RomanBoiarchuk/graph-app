import Graph from "./Graph";
import {useSubscription} from "@apollo/client";
import {GRAPH_SUBSCRIPTION} from "../graphql/graph.subscriptions";
import {CircularProgress} from "@mui/material";
import {useEffect, useState} from "react";

const Main = () => {
    const [skip, setSkip] = useState(true);
    useEffect(() => {
        setSkip(false)
    }, []);
    const {data, loading, error} = useSubscription(GRAPH_SUBSCRIPTION, {skip, shouldResubscribe: true});
    const nodes = data?.graph.vertexes.map(vertex => ({id: vertex.id, text: vertex.name}));
    const edges = data?.graph.edges.map(edge => ({id: edge.id, text: `${edge.weight}`, from: edge.from, to: edge.to}));
    return (
        <div className="Main">
            {loading &&
                <div style={{display: "flex", justifyContent: "center", width: "100%", marginTop: "45vh"}}>
                    <CircularProgress/>
                </div>
            }
            {!loading &&
                <Graph
                    height={window.innerHeight}
                    width={window.innerWidth}
                    nodes={nodes}
                    edges={edges}
                />}
        </div>
    )
}

export default Main;
