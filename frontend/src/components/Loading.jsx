import { SquareLoader } from 'react-spinners';
/**
 * @author Louis Figes
 */
export default function Loading() {
    return (
      <div className="flex items-center justify-center h-full w-full">
          <SquareLoader loading={true} color={'#687387'} />
      </div>
    );
}