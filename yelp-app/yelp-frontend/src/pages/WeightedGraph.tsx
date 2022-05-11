import { useCallback, useState } from 'react';
import ButtonLoader from '../components/ButtonLoader';
import Header from '../components/Header';
import axios from 'axios';
import Loader from '../components/Loader';
import { Graph } from 'react-d3-graph';
import { LOCK } from '../types/interfaces';

const WeightedGraph = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [isReady, setIsReady] = useState<boolean>();
  const [isReload, setIsReload] = useState<boolean>();
  const [isStatic, setIsStatic] = useState<boolean>(false);
  const [lock, setLock] = useState<LOCK>('Lock');

  const [graphData, setGraphData] = useState<any>();
  const [chosenNodeIds, setChosenNodeIds] = useState<String[]>([]);
  const [shortestPathNodes, setShortestPathNodes] = useState<String[]>([]);
  const [shortestPaths, setshortestPaths] = useState<any>();

  const lockGraph = useCallback(() => {
    if (lock === 'Lock') {
      setLock('Unlock');
    } else {
      setLock('Lock');
    }
    setIsStatic(!isStatic);
  }, [isStatic]);

  const fetchGraphData = async () => {
    setLock('Lock');
    setIsStatic(false);
    setIsLoading(true);
    const res = await axios.get(
      'http://localhost:8080/yelpdata/graph/fetch/rd3g'
    );
    setGraphData(res.data);
    setIsReady(true);
    setIsLoading(false);
  };

  const findPath = async () => {
    let shortestNodes: string[] = [];
    const res = await axios.get(
      `http://localhost:8080/yelpdata/graph/fetch/shortest-path/${chosenNodeIds[0]}/${chosenNodeIds[1]}`
    );
    setshortestPaths(res.data.shortestPaths);
    setShortestPathNodes(res.data.shortestPathNodes);
  };
  console.log(shortestPathNodes);
  console.log(shortestPaths);

  const graphConfig: any = {
    nodeHighlightBehavior: true,
    linkHighlightBehavior: true,
    height: 600,
    width: 1000,
    maxZoom: 5,
    minZoom: 0.4,
    highlightOpacity: 0.3,
    staticGraph: isStatic,
    d3: {
      alphaTarget: 0,
      gravity: -400,
    },
    node: {
      color: 'lightgreen',
      highlightStrokeColor: 'lightblue',
      size: 150,
      fontSize: 12,
      highlightFontSize: 12,
      labelPosition: 'bottom',
      mouseCursor: 'grab',
    },
    link: {
      highlightColor: 'lightblue',
      strokeWidth: 2,
      semanticStrokeWidth: true,
      strokeLinecap: 'butt',
    },
  };

  const onClickNode = (nodeId: string) => {
    // setChosenNodeId(nodeId);
  };

  const onDoubleClickNode = (nodeId: any) => {
    if (chosenNodeIds.length >= 2) chosenNodeIds.length = 0;
    chosenNodeIds.push(nodeId);
    setChosenNodeIds(chosenNodeIds);
    window.alert('added to array');
  };
  console.log(chosenNodeIds);

  const onMouseOverNode = () => {
    // setIsStatic(true);
  };
  const onMouseOutNode = () => {
    // setIsStatic(false);
  };

  const onMouseOverLink = function (source: any, target: any) {
    window.alert(`Mouse over in link between ${source} and ${target}`);
  };

  const onNodePositionChange = function (nodeId: any, x: any, y: any) {
    window.alert(`Node ${nodeId} moved to new position x= ${x} y= ${y}`);
  };

  return (
    <div>
      <div className='bg-slate-200 min-h-screen'>
        <div className='flex w-full justify-center flex-col items-center'>
          <div className='flex justify-center items-center w-full flex-col overflow-auto '>
            {isReady ? (
              <div></div>
            ) : (
              <div>
                <div className='w-full mb-8'>
                  <Header />
                </div>
                <div className='bg-white h-24 flex justify-center items-center w-[750px] rounded-lg drop-shadow-2xl '>
                  {isLoading ? (
                    <ButtonLoader />
                  ) : (
                    <div className='flex flex-col'>
                      <button
                        onClick={fetchGraphData}
                        className='cursor-pointer transition-all bg-indigo-500 px-10  shadow-2xl hover:drop-shadow-lg rounded-xl py-2 font-bold text-white hover:bg-indigo-600'
                      >
                        Fetch Random Graph
                      </button>
                    </div>
                  )}
                </div>
              </div>
            )}

            <div>
              {isReady ? (
                <div className='h-[95vh] drop-shadow-xl bg-slate-50 flex justify-center flex-col rounded-xl m-4'>
                  <div className='text-center pt-2 pb-2 font-semibold text-xl'>
                    YELP BUSINESS GRAPH
                  </div>
                  <div className=' h-[800px] w-[1000px] transition-all'>
                    {isLoading ? (
                      <div className='h-full flex justify-center'>
                        <Loader />
                      </div>
                    ) : (
                      <div>
                        <Graph
                          id='graph-id' // id is mandatory, if no id is defined rd3g will throw an error
                          data={graphData}
                          config={graphConfig}
                          onClickNode={onClickNode}
                          onDoubleClickNode={onDoubleClickNode}
                          onMouseOverNode={onMouseOverNode}
                          onMouseOutNode={onMouseOutNode}
                          // onMouseOverLink={() => console.log(`Show link information`)}
                        />
                      </div>
                    )}
                  </div>
                  <div className='flex justify-center'>
                    <div className='flex flex-col'>
                      <div>
                        <button
                          className='bg-indigo-500 mr-1 transition-all text-center w-36 rounded-md text-white mt-1 hover:bg-indigo-600'
                          onClick={lockGraph}
                        >
                          {lock} graph
                        </button>
                        <button
                          className='bg-indigo-500 ml-1 transition-all text-center w-36 rounded-md text-white mt-1 hover:bg-indigo-600'
                          onClick={() => setIsReload(!isReload)}
                        >
                          Reform graph
                        </button>
                      </div>
                      <div>
                        <button
                          className='bg-indigo-500 mr-1 transition-all text-center w-36 rounded-md text-white mt-1 hover:bg-indigo-600'
                          onClick={findPath}
                        >
                          Find Path
                        </button>
                        <button
                          className='bg-indigo-500 ml-1 transition-all text-center w-36 rounded-md text-white mt-1 mb-2 hover:bg-indigo-600'
                          onClick={fetchGraphData}
                        >
                          Fetch new graph
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              ) : (
                <div className='mt-32'></div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default WeightedGraph;
