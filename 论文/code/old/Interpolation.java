package bll.simulation.danji;

public class Interpolation {
	
	
	public static double eslq3_change(double[] x, double[] y, double[][] z,
			int n, int m, double x0, double y0) {
		double[][] zz = new double[n][m];
		// 
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				zz[j][i] = z[i][j];
			}
		}

		int index_x = 0, index_y = 0;

		if (x0 == x[0])
			index_x = 0;
		else

			for (int i = 0; i < n; i++) {
				if ((x[i] < x0) && ((x[i + 1] >= x0))) {
					index_x = i;
					break;
				}
			}

		index_y = 30;
		if (y0 == y[0])
			index_y = 0;
		else
			for (int i = 0; i < m - 1; i++) {
				if ((y[i] < y0) && (y[i + 1] >= y0)) {
					index_y = i;
					break;
				}
			}

		int cx[] = new int[3];
		int cy[] = new int[3];
		if (index_x == 0) {
			cx[0] = index_x;
			cx[1] = index_x + 1;
			cx[2] = index_x + 2;

		} else {
			if (index_x == n - 2) {
				cx[0] = index_x - 1;
				cx[1] = index_x;
				cx[2] = index_x + 1;

			} else {
				if (Math.abs(x[index_x - 1] - x0) > Math.abs(x[index_x + 2]
						- x0)) {
					cx[0] = index_x;
					cx[1] = index_x + 1;
					cx[2] = index_x + 2;
				} else {
					cx[0] = index_x - 1;
					cx[1] = index_x;
					cx[2] = index_x + 1;
				}
			}
		}

		if (index_y == 0) {
			cy[0] = index_y;
			cy[1] = index_y + 1;
			cy[2] = index_y + 2;
		} else {
			if (index_y == m - 2) {
				cy[0] = index_y - 1;
				cy[1] = index_y;
				cy[2] = index_y + 1;
			} else {
				if (Math.abs(y[index_y - 1] - y0) > Math.abs(y[index_y + 2]
						- y0)) {
					cy[0] = index_y;
					cy[1] = index_y + 1;
					cy[2] = index_y + 2;
				} else {
					cy[0] = index_y - 1;
					cy[1] = index_y;
					cy[2] = index_y + 1;
				}
			}
		}

		double u, v, w, value;
		u = zz[cx[0]][cy[0]] * ((x0 - x[cx[1]]) * (x0 - x[cx[2]]))
				/ ((x[cx[0]] - x[cx[1]]) * (x[cx[0]] - x[cx[2]]))
				+ zz[cx[1]][cy[0]] * ((x0 - x[cx[0]]) * (x0 - x[cx[2]]))
				/ ((x[cx[1]] - x[cx[0]]) * (x[cx[1]] - x[cx[2]]))
				+ zz[cx[2]][cy[0]] * ((x0 - x[cx[1]]) * (x0 - x[cx[0]]))
				/ ((x[cx[2]] - x[cx[1]]) * (x[cx[2]] - x[cx[0]]));
		v = zz[cx[0]][cy[1]] * ((x0 - x[cx[1]]) * (x0 - x[cx[2]]))
				/ ((x[cx[0]] - x[cx[1]]) * (x[cx[0]] - x[cx[2]]))
				+ zz[cx[1]][cy[1]] * ((x0 - x[cx[0]]) * (x0 - x[cx[2]]))
				/ ((x[cx[1]] - x[cx[0]]) * (x[cx[1]] - x[cx[2]]))
				+ zz[cx[2]][cy[1]] * ((x0 - x[cx[1]]) * (x0 - x[cx[0]]))
				/ ((x[cx[2]] - x[cx[1]]) * (x[cx[2]] - x[cx[0]]));
		w = zz[cx[0]][cy[2]] * ((x0 - x[cx[1]]) * (x0 - x[cx[2]]))
				/ ((x[cx[0]] - x[cx[1]]) * (x[cx[0]] - x[cx[2]]))
				+ zz[cx[1]][cy[2]] * ((x0 - x[cx[0]]) * (x0 - x[cx[2]]))
				/ ((x[cx[1]] - x[cx[0]]) * (x[cx[1]] - x[cx[2]]))
				+ zz[cx[2]][cy[2]] * ((x0 - x[cx[1]]) * (x0 - x[cx[0]]))
				/ ((x[cx[2]] - x[cx[1]]) * (x[cx[2]] - x[cx[0]]));

		value = u * ((y0 - y[cy[1]]) * (y0 - y[cy[2]]))
				/ ((y[cy[0]] - y[cy[1]]) * (y[cy[0]] - y[cy[2]])) + v
				* ((y0 - y[cy[0]]) * (y0 - y[cy[2]]))
				/ ((y[cy[1]] - y[cy[0]]) * (y[cy[1]] - y[cy[2]])) + w
				* ((y0 - y[cy[1]]) * (y0 - y[cy[0]]))
				/ ((y[cy[2]] - y[cy[1]]) * (y[cy[2]] - y[cy[0]]));
		return value;
	}


}
