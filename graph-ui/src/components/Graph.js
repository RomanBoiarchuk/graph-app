import {Canvas, Edge, Node} from "reaflow";
import {useState} from "react";
import {Box, Button, Modal, TextField, Typography} from "@mui/material";
import {createVertex, removeVertex} from "../service/vertex.service";
import {createEdge, removeEdge} from "../service/edge.service";
import {findShortestPath} from "../service/path.finding.service";

const modalStyle = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 300,
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 4,
};

const Graph = ({height, width, nodes, edges}) => {
    const [nodeSelections, setNodeSelections] = useState([]);
    const [edgeSelections, setEdgeSelections] = useState([]);
    const [addVertexState, setAddVertexState] = useState(null);
    const [addEdgeState, setAddEdgeState] = useState(null);
    const [path, setPath] = useState([]);

    console.log({path});

    const handleNodeClick = node => {
        if (nodeSelections.length < 2) {
            setNodeSelections([...nodeSelections, node.id]);
        }
    };

    const handleEdgeClick = edge => {
        if (edgeSelections.length < 1) {
            setEdgeSelections([edge.id]);
        }
    };

    const handleCanvasClick = () => {
        setNodeSelections([]);
        setEdgeSelections([]);
        setPath([]);
    };

    const handleNodeRemoval = node => {
        removeVertex(node.id).then(() => {
            setNodeSelections(nodeSelections.filter(selection => node.id !== selection));
        });
    };

    const handleEdgeRemoval = edge => {
        removeEdge(edge.id).then(() => {
            setEdgeSelections(edgeSelections.filter(selection => edge.id !== selection));
        });
    };

    const handleAddVertex = () => {
        createVertex(addVertexState.name).then(() => {
            setAddVertexState(null);
        });
    };

    const handleAddEdge = () => {
        createEdge(addEdgeState.weight, addEdgeState.fromNode.id, addEdgeState.toNode.id)
            .then(() => {
                setAddEdgeState(null);
            });
    };

    const handleModalClose = () => {
        setAddVertexState(null);
        setAddEdgeState(null);
    };

    const handleNodeLink = (event, fromNode, toNode) => {
        const isPresent = edges.filter(
            e => e.from === fromNode.id && e.to === toNode.id
                || e.to === fromNode.id && e.from === toNode.id
        ).length !== 0;

        if (!isPresent) {
            setAddEdgeState({
                weight: 1,
                fromNode,
                toNode
            });
        }
    };

    const handleShortestPath = () => {
        const [sourceId, destinationId] = nodeSelections;
        findShortestPath(sourceId, destinationId)
            .then(response => response.json())
            .then(value => {
                setPath(value);
                setNodeSelections([]);
                setEdgeSelections([]);
            });
    };

    const isHighlighted = edge => {
        const sourceIndex = path.findIndex(vertexId => edge.source === vertexId);
        if (sourceIndex < 0) {
            return false;
        }
        if (sourceIndex === 0) {
            return path[sourceIndex + 1] === edge.target;
        } else if (sourceIndex === path.length - 1) {
            return path[sourceIndex - 1] === edge.target;
        }
        return path[sourceIndex + 1] === edge.target || path[sourceIndex - 1] === edge.target;
    };

    return (
        <>
            <Canvas
                height={height}
                maxWidth={width - 250}
                arrow={null}
                nodes={nodes}
                edges={edges}
                selections={[...nodeSelections, ...edgeSelections]}
                onNodeLink={handleNodeLink}
                onCanvasClick={handleCanvasClick}
                node={node => (
                    path.includes(node.id) ?
                        <Node
                            onClick={(event, node) => handleNodeClick(node)}
                            onRemove={(event, node) => handleNodeRemoval(node)}
                            className="Highlighted-node"
                        />
                        :
                        <Node
                            onClick={(event, node) => handleNodeClick(node)}
                            onRemove={(event, node) => handleNodeRemoval(node)}
                        />
                )}
                edge={edge => (
                    isHighlighted(edge) ?
                        <Edge
                            onClick={(event, edge) => handleEdgeClick(edge)}
                            onRemove={(event, edge) => handleEdgeRemoval(edge)}
                            className="Highlighted-edge"
                        />
                        :
                        <Edge
                            onClick={(event, edge) => handleEdgeClick(edge)}
                            onRemove={(event, edge) => handleEdgeRemoval(edge)}
                        />
                )}
                layoutOptions={{
                    'elk.nodeLabels.placement': 'INSIDE V_CENTER H_RIGHT',
                    'elk.algorithm': 'org.eclipse.elk.layered',
                    'elk.direction': 'DOWN',
                    nodeLayering: 'INTERACTIVE',
                    'org.eclipse.elk.edgeRouting': 'ORTHOGONAL',
                    'elk.layered.unnecessaryBendpoints': 'true',
                    'elk.layered.spacing.edgeNodeBetweenLayers': '20',
                    'org.eclipse.elk.layered.nodePlacement.bk.fixedAlignment': 'BALANCED',
                    'org.eclipse.elk.layered.cycleBreaking.strategy': 'DEPTH_FIRST',
                    'org.eclipse.elk.insideSelfLoops.activate': 'true',
                    separateConnectedComponents: 'false',
                    'spacing.componentComponent': '20',
                    spacing: '25',
                    'spacing.nodeNodeBetweenLayers': '20'
                }}
            />
            <div style={{width: 250, alignItems: "center", display: "flex"}}>
                <div style={{display: "flex", flexDirection: "column", gap: 10}}>
                    <Button variant="contained" disabled={nodeSelections.length !== 2} onClick={handleShortestPath}>Find
                        Shortest Path</Button>
                    <Button variant="contained" onClick={() => setAddVertexState({name: ''})}>Add Vertex</Button>
                </div>
            </div>
            <Modal
                open={addVertexState !== null || addEdgeState !== null}
                onClose={handleModalClose}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box sx={modalStyle}>
                    <Typography id="modal-modal-title" variant="h6" component="h2">
                        {addVertexState !== null && `New Vertex`}
                    </Typography>
                    {addVertexState !== null && (
                        <>
                            <TextField
                                label="Name"
                                variant="standard"
                                margin="normal"
                                onChange={e => setAddVertexState({...addVertexState, name: e.target.value})}
                            />
                            <Button
                                disabled={addVertexState?.name?.length < 2}
                                variant="contained"
                                onClick={handleAddVertex}>Add Vertex</Button>
                        </>
                    )}
                    {addEdgeState !== null && (
                        <>
                            <Typography id="modal-modal-description" sx={{mt: 2}}>
                                {`${addEdgeState.fromNode.text} - ${addEdgeState.toNode.text}`}
                            </Typography>
                            <TextField
                                label="Weight"
                                type="number"
                                InputProps={{inputProps: {min: 1}}}
                                defaultValue={1}
                                margin="normal"
                                onChange={e => setAddEdgeState({...addEdgeState, weight: e.target.value})}
                            />
                            <Button
                                disabled={isNaN(addEdgeState.weight) || addEdgeState.weight <= 0}
                                variant="contained"
                                onClick={handleAddEdge}>Add Edge</Button>
                        </>
                    )}
                </Box>
            </Modal>
        </>
    )
}

export default Graph;
