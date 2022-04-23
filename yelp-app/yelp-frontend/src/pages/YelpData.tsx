import { useEffect, useState } from 'react';
import axios from 'axios';
import SearchIcon from '@material-ui/icons/Search';
import Header from '../components/Header';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import { IBusiness, IBusinessList } from '../types/interfaces';
import Loader from '../components/Loader';
import Alert from '@material-ui/lab/Alert';

const YelpData = () => {
  let similarBs: IBusinessList[] = [];
  let allBs: IBusinessList[] = [];

  const [businessName, setBusinessName] = useState('');
  const [allBList, setAllBList] = useState<IBusinessList[]>();
  const [similarBList, setSimilarBList] = useState<IBusinessList[]>();
  const [targetBusiness, setTargetBusiness] = useState<IBusiness>();
  const [isLoading, setIsLoading] = useState<Boolean>(false);
  const [isReady, setIsReady] = useState<Boolean>(false);
  const [catToString, setCatToString] = useState('');
  const [isValid, setIsValid] = useState(true);

  const getSimilarBs = async (e: any) => {
    e.preventDefault();
    if (businessName != '') {
      setIsLoading(true);
      setIsReady(false);
      try {
        const similarBusinesses = await axios.get(
          `http://localhost:8080/yelpdata/similar/${businessName}`
        );

        const businessByName = await axios.get(
          `http://localhost:8080/yelpdata/${businessName}`
        );

        console.log(similarBusinesses.data);

        await similarBusinesses.data.map((data: any) => {
          similarBs.push(data);
        });

        setAllBList(similarBs);
        setSimilarBList(similarBs);

        similarBs = [];
        setBusinessName('');
        setIsLoading(false);
        setIsReady(true);
        setIsValid(true);
      } catch (err) {
        setIsValid(false);
        console.log(err);
      }
    }
  };

  const getAllBs = async () => {
    const res = await axios.get(
      'http://localhost:8080/yelpdata/get-all-businesses'
    );

    res.data.map((data: any) => {
      allBs.push(data);
    });
    setAllBList(allBs);
  };

  useEffect(() => {
    getAllBs();
  }, []);

  return (
    <div className=' bg-slate-200 min-h-screen max-h-screen'>
      <div className='flex w-full justify-center flex-col items-center'>
        <div className=' w-full mb-8'>
          <Header />
        </div>
        <div className='flex justify-center items-center w-full'>
          <div className='bg-white h-36 flex justify-center items-center w-[750px] rounded-lg drop-shadow-2xl'>
            <form onSubmit={getSimilarBs} className=' flex ml-20 items-center'>
              <SearchIcon className='absolute mx-2 text-slate-400' />
              <input
                type='text'
                className='py-[7px] pl-24 pr-48 w-11/12 border-[1px] shadow-lg rounded-md outline-none text-gray-400'
                placeholder='Business Name...'
                name='businessName'
                value={businessName}
                onChange={(e) => setBusinessName(e.target.value)}
              />
              <button
                type='submit'
                className='cursor-pointer bg-indigo-500 px-10 ml-5 shadow-2xl hover:drop-shadow-lg rounded-xl py-2 font-bold text-white hover:bg-indigo-600 '
              >
                Find
              </button>
            </form>
          </div>
        </div>
        <div className=' h-4/5  '>
          {isValid ? (
            <div>
              <div className='flex justify-center w-full my-5 py-5 max-h-[515px]'>
                {isLoading ? (
                  <Loader />
                ) : (
                  <div className=' w-11/12 max-h-11/12 overflow-auto drop-shadow-xl '>
                    {isReady ? (
                      <div>
                        <Paper>
                          <Table className='w-full '>
                            <TableHead>
                              <TableRow>
                                <TableCell align='center' width='50px'>
                                  Similarity Rate
                                </TableCell>
                                <TableCell align='center' width='200px'>
                                  Business Name
                                </TableCell>
                                <TableCell align='center' width='350px'>
                                  Categories
                                </TableCell>
                                <TableCell align='center' width='50px'>
                                  Reviews
                                </TableCell>
                                <TableCell align='center' width='50px'>
                                  Stars
                                </TableCell>
                                <TableCell align='center' width='200px'>
                                  Address
                                </TableCell>
                              </TableRow>
                            </TableHead>
                            <TableBody>
                              {similarBList?.map((company: any) => (
                                <TableRow key={company.id}>
                                  <TableCell align='center'>
                                    {(company.similarityRate * 100).toFixed(2)}{' '}
                                    %
                                  </TableCell>
                                  <TableCell align='center'>
                                    {company.name}
                                  </TableCell>
                                  <TableCell align='center'>
                                    {company.categories}
                                  </TableCell>
                                  <TableCell align='center'>
                                    {company.reviews}
                                  </TableCell>
                                  <TableCell align='center'>
                                    {company.stars}
                                  </TableCell>
                                  <TableCell align='center'>
                                    {company.address}
                                  </TableCell>
                                </TableRow>
                              ))}
                            </TableBody>
                          </Table>
                        </Paper>
                      </div>
                    ) : (
                      <div></div>
                    )}
                  </div>
                )}
              </div>
            </div>
          ) : (
            <div className=' mt-24'>
              <Alert severity='error'> Business not found </Alert>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default YelpData;
