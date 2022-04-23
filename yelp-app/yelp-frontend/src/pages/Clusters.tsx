import { useRef, useState } from 'react';
import Header from '../components/Header';
import { hierarchy, json, linkHorizontal, select, tree } from 'd3';
import ButtonLoader from '../components/ButtonLoader';

const Clusters = () => {
  const [isLoading, setIsLoading] = useState(false);

  const svgRef: any = useRef();
  const width = 1000;
  const height = 1500;

  const updateClusters = async () => {
    setIsLoading(true);
    const data = await json('http://localhost:8080/yelpdata/fetch-d3-clusters');
    if (data != null) {
      const svg: any = select(svgRef.current);
      const root: any = hierarchy(data);
      const treeLayout: any = tree().size([height, width]);
      treeLayout(root);

      console.log(root.descendants());
      console.log(root.links());

      // links generator
      const linksGenerator: any = linkHorizontal()
        .x((node: any) => node.y)
        .y((node: any) => node.x);

      // nodes
      svg
        .selectAll('.node')
        .data(root.descendants().reverse())
        .join('circle')
        .attr('class', 'node')
        .attr('stroke-linejoin', 'round')
        .attr('r', 3)
        .attr('cx', (node: any) => node.y)
        .attr('cy', (node: any) => node.x)
        .attr('opacity', 0)
        .transition()
        .duration(500)
        .delay((node: any) => node.depth * 500)
        .attr('opacity', 1)
        .attr('transform', (d: any) => {
          `
          rotate(${(d.x * 180) / Math.PI - 90})
          translate(${d.y},0)
          `;
        })
        .attr('fill', (d: any) => (d.children ? '#555' : '#DA9489'));

      // links
      svg
        .selectAll('.link')
        .data(root.links())
        .join('path')
        .attr('class', 'link')
        .attr('fill', 'none')
        .attr('stroke', '#555')
        .attr('stroke-opacity', 0.6)
        .attr('stroke-width', 1.2)
        .attr('d', linksGenerator)
        .attr('stroke-dasharray', function (this: any) {
          const length = this.getTotalLength();
          return `${length} ${length}`;
        })
        .attr('stroke-dashoffset', function (this: any) {
          const length = this.getTotalLength();
          return length;
        })
        .transition()
        .duration(500)
        .delay((linkObj: any) => linkObj.source.depth * 500)
        .attr('stroke-dashoffset', 0);

      // labels
      svg
        .selectAll('.label')
        .data(root.descendants())
        .join('text')
        .text((node: any) => node.data.name)
        .attr('class', 'label')
        .attr('text-anchor', function (d: any) {
          return d.children || d._children ? 'middle' : 'start';
        })
        .attr('font-size', 10)
        .attr('x', function (node: any) {
          return node.children || node._children ? node.y : node.y + 10;
        })
        .attr('y', function (node: any) {
          return node.children || node._children ? node.x + 10 : node.x;
        })
        .attr('opacity', 0)
        .transition()
        .duration(500)
        .delay((node: any) => node.depth * 500)
        .attr('opacity', 1)
        .attr('dy', '.35em');
    }
    setIsLoading(false);
  };

  return (
    <div className='bg-slate-200 '>
      <div className='flex w-full justify-center flex-col items-center h-[2000px]'>
        <div className='w-full mb-8'>
          <Header />
        </div>
        <div className='flex justify-center items-center w-full flex-col h-full overflow-auto '>
          <div className='bg-white h-24 flex justify-center items-center w-[750px] rounded-lg drop-shadow-2xl'>
            {isLoading ? (
              <ButtonLoader />
            ) : (
              <div className='flex flex-col'>
                <button
                  onClick={updateClusters}
                  className='cursor-pointer bg-indigo-500 px-10  shadow-2xl hover:drop-shadow-lg rounded-xl py-2 font-bold text-white hover:bg-indigo-600'
                >
                  Fetch Random Clusters
                </button>
                <small className='text-neutral-400 pt-1'>
                  {' '}
                  This will fetch 5-10 clusters randomly from the server.
                </small>
              </div>
            )}
          </div>

          <div className='w-full h-full'>
            <svg
              ref={svgRef}
              className='w-full pl-24 pt-14 h-full overflow-scroll'
            ></svg>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Clusters;
