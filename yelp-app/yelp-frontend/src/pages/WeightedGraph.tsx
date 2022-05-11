import { useCallback, useEffect, useRef, useState } from 'react';
import ButtonLoader from '../components/ButtonLoader';
import Header from '../components/Header';
import { Graph } from 'react-d3-graph';
import axios from 'axios';
import Loader from '../components/Loader';
import GraphComponent from '../components/GraphComponent';

const WeightedGraph = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [isReady, setIsReady] = useState<boolean>();
  const [isReload, setIsReload] = useState<boolean>();
  const [isStatic, setIsStatic] = useState<boolean>();

  const [graphData, setGraphData] = useState<any>();

  const fetchGraphData = async () => {
    setIsLoading(true);
    const res = await axios.get(
      'http://localhost:8080/yelpdata/graph/fetch/rd3g'
    );
    setGraphData(res.data);
    setIsReady(true);
    setIsLoading(false);
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
                        <GraphComponent graphData={graphData} />
                      </div>
                    )}
                  </div>
                  <div className='flex justify-center'>
                    <div className='flex flex-col'>
                      <button
                        className='bg-indigo-500 transition-all text-center w-36 rounded-md text-white mt-1 hover:bg-indigo-600'
                        onClick={() => setIsReload(!isReload)}
                      >
                        Reform graph
                      </button>
                      <button
                        className='bg-indigo-500 transition-all text-center w-36 rounded-md text-white mt-2 mb-2 hover:bg-indigo-600'
                        onClick={fetchGraphData}
                      >
                        Fetch new graph
                      </button>
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
