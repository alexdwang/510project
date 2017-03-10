package zindex;

import java.util.LinkedList;
import java.util.List;

public class ZEncoder {

    static final int N_DIMS = 5;

    static final int N_BITS = 32;

    private List<long[]> filters;

    public ZEncoder() {
        filters = this.createFilter(N_DIMS, N_BITS);
    }

    public long encode(int[] points) {
        if (points.length != N_DIMS) {
            throw new IllegalNumOfDimensionException("Illegal number of dimension of the coordinate");
        }
        long code = 0;
        for (int i = 0; i < points.length; i++) {
            code |= this.spread(points[i]) << i;
        }

        return code;
    }

    public int[] decode(long code) {
        int[] point = new int[N_DIMS];
        for (int i = 0; i < N_DIMS; i++) {
            point[i] = (int) this.compact(code >> i);
        }

        return point;
    }

    private List<long[]> createFilter(int ndims, int nbits) {
        List<long[]> filters = new LinkedList<>();

        /*
         * Construct the initial mask: this selects just the lower (nbits/ndim)
         * bits of a number
         */
        int width = nbits / ndims;
        long mask = (1 << width) - 1;

        /*
         * Each bit in a coordinate needs to move (ndim * width) positions
         * First get the maximum shifts we need to do:
         */
        long maxShift = getLessThanOrEqualsToPowerOf2(ndims * (width - 1));
        filters.add(new long[] { mask, 0, maxShift });

        /*
         * Now figure out which bits need to be moved by each shift, and build masks
         */
        long shift = maxShift;
        while (shift > 0) {
            mask = 0;
            long shifted = 0;
            long shiftMask = ~(shift - 1);
            for (int bit = 0; bit < width; bit++) {
                long distance = ndims * bit - bit;
                shifted |= (shift & distance);
                mask |= 1 << bit << (distance & shiftMask);
            }
            if (shifted != 0) {
                filters.add(new long[] { mask, shift, shift >> 1 });
            }
            shift >>= 1;
        }

        // Set the last right shift to zero for compact operation
        filters.get(filters.size()-1)[2] = 0;
        return filters;
    }

    private long getLessThanOrEqualsToPowerOf2(long num) {
        num |= (num >> 1);
        num |= (num >> 2);
        num |= (num >> 4);
        num |= (num >> 8);
        num |= (num >> 16);
        num |= (num >> 32);
        return num - (num >> 1);
    }

    private long spread(long x) {
        // Structure of a filter: [mask, shift, num_of_right_shifts]
        for (long[] filter : this.filters) {
            x = (x | x << filter[1]) & filter[0];
        }
        return x;
    }

    private long compact(long x) {
        // Structure of a filter: [mask, shift, num_of_right_shifts]
        for (int i = this.filters.size()-1; i >= 0; i--) {
            long[] filter = filters.get(i);
            x = (x | (x >> filter[2])) & filter[0];
        }
        return x;
    }
}
