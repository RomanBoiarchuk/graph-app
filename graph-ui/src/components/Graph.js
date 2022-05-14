import {Canvas, Edge, Node, useUndo} from "reaflow";
import {useState} from "react";
import {Box, Button, Modal, TextField, Typography} from "@mui/material";

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

const Graph = ({height, width}) => {
    const [nodeSelections, setNodeSelections] = useState([]);
    const [edgeSelections, setEdgeSelections] = useState([]);
    const [addVertexState, setAddVertexState] = useState(null);
    const [addEdgeState, setAddEdgeState] = useState(null);
    const nodesHardCode = [
        {
            id: '1',
            text: 'Node 1'
        },
        {
            id: '2',
            text: 'Node 2'
        },
        {
            id: '3',
            text: 'Node 3'
        },
        {
            id: '4',
            text: 'Node 4'
        },
        {
            id: '5',
            text: 'Node 5'
        }
    ];
    const edgesHardcode = [
        {
            id: '1-2',
            from: '1',
            to: '2',
            text: '1'
        },
        {
            id: '1-3',
            from: '1',
            to: '3',
            text: '1'
        },
        {
            id: '1-4',
            from: '1',
            to: '4',
            text: '1'
        },
        {
            id: '2-4',
            from: '2',
            to: '4',
            text: '2'
        }
    ]
    const [nodes, setNodes] = useState(nodesHardCode);
    const [edges, setEdges] = useState(edgesHardcode);

    const {undo, redo, canUndo, canRedo, history, clear, count} = useUndo({
        nodes,
        edges,
        onUndoRedo: (state) => {
            console.log('Undo / Redo', state);
            console.log('history', history());
            setEdges(state.edges);
            setNodes(state.nodes);
        }
    });

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
    };

    const handleNodeRemoval = node => {
        // TODO call API
        setEdges(edges.filter(e => e.from !== node.id && e.to !== node.id));
        setNodes(nodes.filter(n => n.id !== node.id));
        setNodeSelections([]);
        setEdgeSelections([]);
    };

    const handleEdgeRemoval = edge => {
        // TODO call API
        setEdges(edges.filter(e => e.id !== edge.id));
        setNodeSelections([]);
        setEdgeSelections([]);
    };

    const handleAddVertex = () => {
        // TODO call API
        const newNode = {
            id: `${Math.random()}`,
            text: addVertexState.name
        }
        setNodes([...nodes, newNode]);
        setAddVertexState(null);
    };

    const handleAddEdge = () => {
        // TODO call API
        const newEdge = {
            id: `${Math.random()}`,
            text: addEdgeState.weight,
            from: addEdgeState.fromNode.id,
            to: addEdgeState.toNode.id
        }
        setEdges([...edges, newEdge]);
        setAddEdgeState(null);
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

    return (
        <>
            <Canvas
                height={height}
                maxWidth={width - 250}
                arrow={null}
                nodes={nodes}
                edges={edges}
                selections={[...nodeSelections, ...edgeSelections]}
                onLayoutChange={layout => console.log('Layout', layout)}
                onNodeLink={handleNodeLink}
                onCanvasClick={handleCanvasClick}
                node={
                    <Node
                        onClick={(event, node) => handleNodeClick(node)}
                        onRemove={(event, node) => handleNodeRemoval(node)}
                    />
                }
                edge={
                    <Edge
                        onClick={(event, edge) => handleEdgeClick(edge)}
                        onRemove={(event, edge) => handleEdgeRemoval(edge)}
                    />
                }
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
                    <Button variant="contained" disabled={nodeSelections.length !== 2}>Find Shortest Path</Button>
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
