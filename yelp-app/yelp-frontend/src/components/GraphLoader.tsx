const GraphLoader = () => {
  return (
    <div className='flex justify-center items-center py-3'>
      <div className='animate-spin rounded-full h-32 w-32 border-b-2 border-green-300' />
      <div className='ml-5 text-lg text-slate-200'>Fetching data...</div>
    </div>
  );
};

export default GraphLoader;
