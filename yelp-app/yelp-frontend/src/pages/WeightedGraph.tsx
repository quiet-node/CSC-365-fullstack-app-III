import React, { useState } from 'react';
import ButtonLoader from '../components/ButtonLoader';
import Header from '../components/Header';
import { Graph } from 'react-d3-graph';

const WeightedGraph = () => {
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
        </div>
      </div>
    </div>
  );
};

export default WeightedGraph;
