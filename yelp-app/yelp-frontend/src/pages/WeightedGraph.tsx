import React, { useState } from 'react';
import ButtonLoader from '../components/ButtonLoader';
import Header from '../components/Header';
import { Graph } from 'react-d3-graph';

const WeightedGraph = () => {
  // graph payload (with minimalist structure)
  const data = {
    nodes: [
      { id: '1' },
      { id: '2' },
      { id: '3' },
      { id: '4' },
      { id: '5' },
      { id: '6' },
      { id: '7' },
      { id: '8' },
    ],
    links: [
      { source: '1', target: '2' },
      { source: '2', target: '3' },
      { source: '3', target: '4' },
      { source: '4', target: '5' },
      { source: '5', target: '6' },
      { source: '6', target: '7' },
      { source: '7', target: '8' },
    ],
  };

  const graphConfig = {
    nodeHighlightBehavior: true,
    height: 800,
    width: 1000,
    maxZoom: 5,
    minZoom: 0.4,
    d3: {
      alphaTarget: 0.05,
      gravity: -200,
    },
    node: {
      color: 'lightgreen',
      highlightStrokeColor: 'lightblue',
      size: 150,
      fontSize: 12,
      highlightFontSize: 12,
      fontWeight: 'bold',
      highlightFontWeight: 'bold',
    },
    link: {
      highlightColor: 'lightblue',
      strokeWidth: 2,
    },
  };

  const onClickNode = function (nodeId: any, node: any) {
    window.alert('Clicked node ${nodeId} in position (${node.x}, ${node.y})');
  };

  const onDoubleClickNode = function (nodeId: any, node: any) {
    window.alert(
      'Double clicked node ${nodeId} in position (${node.x}, ${node.y})'
    );
  };

  const onMouseOverNode = function (nodeId: any, node: { x: any; y: any }) {
    window.alert(
      `Mouse over node ${nodeId} in position (${node.x}, ${node.y})`
    );
  };

  const onMouseOverLink = function (source: any, target: any) {
    window.alert(`Mouse over in link between ${source} and ${target}`);
  };

  const onNodePositionChange = function (nodeId: any, x: any, y: any) {
    window.alert(`Node ${nodeId} moved to new position x= ${x} y= ${y}`);
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
          <div className='h-max-screen drop-shadow-xl bg-slate-50 flex justify-center flex-col rounded-xl m-8'>
            <div className='text-center pt-5 pb-5'>YELP BUSINESS GRAPH</div>
            <div className=' h-[800px] w-[1000px] '>
              <Graph
                id='graph-id' // id is mandatory, if no id is defined rd3g will throw an error
                data={data}
                config={graphConfig}
                onClickNode={() => console.log(`Choose this node`)}
                onDoubleClickNode={() => console.log(`Focus zoom to a node`)}
                onMouseOverNode={() => console.log(`Show node information`)}
                onMouseOverLink={() => console.log(`Show link information`)}
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default WeightedGraph;
