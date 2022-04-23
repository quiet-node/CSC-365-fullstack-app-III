import { useEffect, useState } from 'react';
import ResizeObserver from 'resize-observer-polyfill';

const useResizeObserver = (ref: { current: any }) => {
  const [dimensions, setDimensions] = useState(null);
  useEffect(() => {
    const observeTarget = ref.current;
    const resizeObserver = new ResizeObserver((entries: any) => {
      entries.forEach((entry: any) => {
        return setDimensions(entry.contentRect);
      });
    });
    resizeObserver.observe(observeTarget);
    return () => {
      resizeObserver.unobserve(observeTarget);
    };
  }, [ref]);
  return dimensions;
};

export default useResizeObserver;
