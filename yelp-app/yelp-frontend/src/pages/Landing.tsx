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
          <div className='bg-white h-36 flex justify-center items-center w-[750px] rounded-lg drop-shadow-2xl'>
            <div className='flex items-center'>
              <Link
                to='/yelp-data'
                className='cursor-pointer bg-indigo-500 px-10 ml-5 shadow-2xl hover:drop-shadow-lg rounded-lg py-2 font-bold text-white hover:bg-indigo-600 '
              >
                Look up a business
              </Link>
              <Link
                to='/clusters'
                className='cursor-pointer bg-indigo-500 px-10 ml-5 shadow-2xl hover:drop-shadow-lg rounded-lg py-2 font-bold text-white hover:bg-indigo-600 '
              >
                Fetch random clusters
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Landing;
