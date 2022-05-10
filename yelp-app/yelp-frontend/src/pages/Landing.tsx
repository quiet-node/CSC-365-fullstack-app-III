import Header from '../components/Header';
import { Link } from 'react-router-dom';

const Landing = () => {
  return (
    <div className=' bg-slate-200 min-h-screen max-h-screen'>
      <div className='flex w-full justify-center flex-col items-center'>
        <div className='w-full mb-8 '>
          <Header />
        </div>
        <div className='flex justify-center items-center w-full h-36 mt-36'>
          <div className='bg-white h-52 flex justify-center items-center w-[750px] rounded-lg drop-shadow-2xl'>
            <div className='flex flex-col items-center'>
              <Link
                to='/yelp-data'
                className='cursor-pointer w-72 bg-indigo-500 text-center ml-5 shadow-2xl hover:drop-shadow-lg rounded-lg py-2 font-bold text-white hover:bg-indigo-600 '
              >
                Look up a business
              </Link>
              <Link
                to='/clusters'
                className='cursor-pointer w-72 bg-indigo-500 text-center ml-5 mt-5 shadow-2xl hover:drop-shadow-lg rounded-lg py-2 font-bold text-white hover:bg-indigo-600 '
              >
                Fetch random clusters
              </Link>
              <Link
                to='/graph'
                className='cursor-pointer w-72 bg-indigo-500 text-center ml-5 mt-5 shadow-2xl hover:drop-shadow-lg rounded-lg py-2 font-bold text-white hover:bg-indigo-600 '
              >
                Find shortest path
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Landing;
