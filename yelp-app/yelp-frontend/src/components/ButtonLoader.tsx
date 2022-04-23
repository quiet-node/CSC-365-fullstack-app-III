const ButtonLoader = () => {
  return (
    <div className='flex justify-center items-center py-3'>
      <div className='animate-spin rounded-full h-10 w-10 border-b-2 border-violet-400' />
      <div className='ml-5 text-sm'>Fetching data...</div>
    </div>
  );
};

export default ButtonLoader;
