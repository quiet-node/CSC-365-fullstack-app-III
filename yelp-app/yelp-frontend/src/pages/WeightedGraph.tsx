import { useCallback, useState } from 'react';
import ButtonLoader from '../components/ButtonLoader';
import Header from '../components/Header';
import axios from 'axios';
import Loader from '../components/Loader';
import { Graph } from 'react-d3-graph';
import { LOCK } from '../types/interfaces';
import GraphLoader from '../components/GraphLoader';

const WeightedGraph = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [isReady, setIsReady] = useState<boolean>();
  const [isShortestPathReady, setIsShortestPathReady] = useState<boolean>();
  const [isReload, setIsReload] = useState<boolean>();
  const [isStatic, setIsStatic] = useState<boolean>(false);
  const [lock, setLock] = useState<LOCK>('Lock');

  const [graphData, setGraphData] = useState<any>();
  const [chosenNodeIds, setChosenNodeIds] = useState<String[]>([]);
  const [shortestPathLinks, setShortestPathLinks] = useState<any>();
  const [shortestPathNodes, setShortestPathNodes] = useState<any>();

  const lockGraph = useCallback(() => {
    if (lock === 'Lock') {
      window.alert('Graph locked.');
      setLock('Unlock');
    } else {
      window.alert('Graph unlocked.');
      setLock('Lock');
    }
    setIsStatic(!isStatic);
  }, [isStatic]);

  const fetchGraphData = async () => {
    setLock('Lock');
    setIsStatic(false);
    setIsLoading(true);
    chosenNodeIds.length = 0;
    const res = await axios.get(
      'http://localhost:8080/yelpdata/graph/fetch/rd3g'
    );
    setGraphData(res.data);
    setIsReady(true);
    setIsLoading(false);
    setIsShortestPathReady(false);
  };

  const findPath = async () => {
    if (chosenNodeIds.length < 2) {
      const alert = `Please pick two nodes to process this request.\nCurrent chosen node IDs= [${
        chosenNodeIds.length == 0 ? '' : chosenNodeIds[0]
      }]`;
      window.alert(alert);
      return;
    }
    if (!isStatic) {
      window.alert('Please lock your graph to process this request.');
      return;
    }
    setIsShortestPathReady(false);
    const res = await axios.get(
      `http://localhost:8080/yelpdata/graph/fetch/shortest-path/${chosenNodeIds[0]}/${chosenNodeIds[1]}`
    );
    setShortestPathLinks(res.data.shortestPaths);
    setShortestPathNodes(res.data.shortestPathNodes);
    setIsShortestPathReady(true);
  };

  const shortestPathGraphData: any = {
    nodes: shortestPathNodes,
    links: shortestPathLinks,
  };

  const graphConfig: any = {
    nodeHighlightBehavior: true,
    linkHighlightBehavior: true,
    height: 800,
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
      fontColor: '#e5e7eb',
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
  const graphConfig2: any = {
    nodeHighlightBehavior: true,
    linkHighlightBehavior: true,
    height: 350,
    width: 350,
    maxZoom: 5,
    minZoom: 0.4,
    d3: {
      alphaTarget: 0,
      gravity: -400,
    },
    node: {
      color: 'lightgreen',
      highlightStrokeColor: 'lightblue',
      fontColor: '#e5e7eb',
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

  const onDoubleClickNode = (nodeId: any) => {
    if (chosenNodeIds.length >= 2) chosenNodeIds.length = 0;
    chosenNodeIds.push(nodeId);
    setChosenNodeIds(chosenNodeIds);
    window.alert(
      `Added node to chosenNodeIds array.\nchosen node IDs=[source: ${
        chosenNodeIds.length == 1
          ? chosenNodeIds[0]
          : `${chosenNodeIds[0]}, target: ${chosenNodeIds[1]}`
      }]`
    );
  };

  return (
    <div>
      <div className='bg-slate-200 min-h-screen'>
        <div className='flex w-full justify-center flex-col items-center'>
          <div className='flex justify-center items-center w-full flex-col '>
            {isReady ? (
              <div></div>
            ) : (
              <div>
                <div className='w-full mb-8'>
                  <Header />
                </div>
                <div className='bg-white h-24 flex justify-center items-center w-[750px] rounded-lg shadow-lg '>
                  {isLoading ? (
                    <ButtonLoader />
                  ) : (
                    <div className='flex flex-col'>
                      <button
                        onClick={fetchGraphData}
                        className='cursor-pointer transition-all bg-indigo-500 px-10 hover:drop-shadow-lg rounded-xl py-2 font-bold text-white hover:bg-indigo-600'
                      >
                        Fetch Random Graph
                      </button>
                    </div>
                  )}
                </div>
              </div>
            )}

            <div className='m-4 relative'>
              {isReady ? (
                <div className='h-[95vh] shadow-md bg-slate-400 flex justify-center flex-col rounded-xl '>
                  <div className='text-center pt-2 pb-2 font-semibold text-xl text-slate-200'>
                    YELP BUSINESS GRAPH
                  </div>
                  <div className=' h-[800px] w-[1000px] transition-all'>
                    {isLoading ? (
                      <div className='h-full flex justify-center align-center'>
                        <GraphLoader />
                      </div>
                    ) : (
                      <div className=''>
                        <div className='z-0'>
                          <Graph
                            id='graph-id' // id is mandatory, if no id is defined rd3g will throw an error
                            data={graphData}
                            config={graphConfig}
                            onDoubleClickNode={onDoubleClickNode}
                          />
                        </div>
                        <div>
                          {isShortestPathReady ? (
                            <div className='w-[330px] h-[400px] absolute bottom-0 left-0'>
                              <Graph
                                id='shortest-path-id'
                                data={shortestPathGraphData}
                                config={graphConfig2}
                              />
                              <div className='text-slate-200 text-center'>
                                Dijkstra Shortest Path
                              </div>
                            </div>
                          ) : (
                            <div></div>
                          )}
                        </div>
                      </div>
                    )}
                  </div>
                  <div className='flex justify-center'>
                    <div className='flex flex-col p-2 mb-2 rounded-xl'>
                      <div>
                        <button
                          className='mr-1 transition-all text-center w-36 rounded-md hover:bg-slate-200 hover:text-indigo-600 bg-slate-100 text-indigo-500'
                          onClick={lockGraph}
                        >
                          {lock} graph
                        </button>
                        <button
                          className=' ml-1 transition-all text-center w-36 rounded-md mt-1 hover:bg-slate-200 hover:text-indigo-600 bg-slate-100 text-indigo-500'
                          onClick={() => setIsReload(!isReload)}
                        >
                          Reform graph
                        </button>
                      </div>
                      <div>
                        <button
                          className=' mr-1 transition-all text-center w-36 rounded-md mt-1 hover:bg-slate-200 hover:text-indigo-600 bg-slate-100 text-indigo-500'
                          onClick={findPath}
                        >
                          Find Path
                        </button>
                        <button
                          className=' ml-1 transition-all text-center w-36 rounded-md mt-1 hover:bg-slate-200 hover:text-indigo-600 bg-slate-100 text-indigo-500'
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
