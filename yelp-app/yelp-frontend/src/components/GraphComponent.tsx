import React, { useCallback, useState } from 'react';
import { Graph } from 'react-d3-graph';

interface Props {
  graphData: any;
  isStatic: boolean;
}

const GraphComponent = ({ graphData, isStatic }: Props): JSX.Element => {
  const [chosenNodeId, setChosenNodeId] = useState<string>('0');

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
    setChosenNodeId(nodeId);
  };
  console.log(chosenNodeId);

  const onDoubleClickNode = (nodeId: any) => {
    // setIsStatic(!isStatic);
  };
  console.log(isStatic);

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
  );
};

export default GraphComponent;
