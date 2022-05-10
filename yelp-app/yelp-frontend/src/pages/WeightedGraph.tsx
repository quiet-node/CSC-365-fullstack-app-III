import React, { useState } from 'react';
import ButtonLoader from '../components/ButtonLoader';
import Header from '../components/Header';
import { Graph } from 'react-d3-graph';

const WeightedGraph = () => {
  // graph payload (with minimalist structure)
  const data = {
    nodes: [{ id: 'Harry' }, { id: 'Sally' }, { id: 'Alice' }],
    links: [
      { source: 'Harry', target: 'Sally' },
      { source: 'Harry', target: 'Alice' },
    ],
  };

  // the graph configuration, you only need to pass down properties
  // that you want to override, otherwise default ones will be used
  const myConfig = {
    nodeHighlightBehavior: true,
    node: {
      color: 'lightgreen',
      size: 120,
      highlightStrokeColor: 'blue',
    },
    link: {
      highlightColor: 'lightblue',
    },
  };

  const onClickGraph = function (event: any) {
    window.alert('Clicked the graph background');
  };

  const onClickNode = function (nodeId: any, node: any) {
    window.alert('Clicked node ${nodeId} in position (${node.x}, ${node.y})');
  };

  const onDoubleClickNode = function (nodeId: any, node: any) {
    window.alert(
      'Double clicked node ${nodeId} in position (${node.x}, ${node.y})'
    );
  };

  const onRightClickNode = function (event: any, nodeId: any, node: any) {
    window.alert(
      'Right clicked node ${nodeId} in position (${node.x}, ${node.y})'
    );
  };

  const onMouseOverNode = function (nodeId: any, node: { x: any; y: any }) {
    window.alert(
      `Mouse over node ${nodeId} in position (${node.x}, ${node.y})`
    );
  };

  const onMouseOutNode = function (nodeId: any, node: { x: any; y: any }) {
    window.alert(`Mouse out node ${nodeId} in position (${node.x}, ${node.y})`);
  };

  const onClickLink = function (source: any, target: any) {
    window.alert(`Clicked link between ${source} and ${target}`);
  };

  const onRightClickLink = function (event: any, source: any, target: any) {
    window.alert('Right clicked link between ${source} and ${target}');
  };

  const onMouseOverLink = function (source: any, target: any) {
    window.alert(`Mouse over in link between ${source} and ${target}`);
  };

  const onMouseOutLink = function (source: any, target: any) {
    window.alert(`Mouse out link between ${source} and ${target}`);
  };

  const onNodePositionChange = function (nodeId: any, x: any, y: any) {
    window.alert(`Node ${nodeId} moved to new position x= ${x} y= ${y}`);
  };

  // Callback that's called whenever the graph is zoomed in/out
  // @param {number} previousZoom the previous graph zoom
  // @param {number} newZoom the new graph zoom
  const onZoomChange = function (previousZoom: any, newZoom: any) {
    window.alert(`Graph is now zoomed at ${newZoom} from ${previousZoom}`);
  };

  const [isLoading, setIsLoading] = useState(false);
  return (
    <div className='bg-slate-200 min-h-screen'>
      <div className='flex w-full justify-center flex-col items-center h-full'>
        <div className='w-full mb-8'>
          <Header />
        </div>
        <div className='flex justify-center items-center w-full flex-col h-full overflow-auto '>
          <div className='bg-white h-24 flex justify-center items-center w-[750px] rounded-lg drop-shadow-2xl '>
            {isLoading ? (
              <ButtonLoader />
            ) : (
              <div className='flex flex-col'>
                <button
                  onClick={() => setIsLoading(true)}
                  className='cursor-pointer bg-indigo-500 px-10  shadow-2xl hover:drop-shadow-lg rounded-xl py-2 font-bold text-white hover:bg-indigo-600'
                >
                  Fetch Random Graph
                </button>
              </div>
            )}
          </div>
          <Graph
            id='graph-id' // id is mandatory, if no id is defined rd3g will throw an error
            data={data}
            config={myConfig}
            onClickGraph={onClickGraph}
            // onClickNode={onClickNode}
            // onDoubleClickNode={onDoubleClickNode}
            // onRightClickNode={onRightClickNode}
            onClickLink={onClickLink}
            onRightClickLink={onRightClickLink}
            // onMouseOverNode={onMouseOverNode}
            // onMouseOutNode={onMouseOutNode}
            onMouseOverLink={onMouseOverLink}
            onMouseOutLink={onMouseOutLink}
            onNodePositionChange={onNodePositionChange}
            onZoomChange={onZoomChange}
          />
          ;
        </div>
      </div>
    </div>
  );
};

export default WeightedGraph;
