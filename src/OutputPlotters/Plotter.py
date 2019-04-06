import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import numpy as np


class Plotter:

    def __init__(self, figure_fn):
        self.__figure_fn__ = figure_fn

    def __read__(self):
        pass

    def plot(self):
        data, centers = self.__read__()

        fig = plt.figure()
        ax = fig.add_subplot(1, 1, 1)

        for i in range(len(data)):
            ax.scatter([x for x, y in data[i]], [y for x, y in data[i]], c=np.random.rand(3, ), s=5)
        for i in range(len(data)):
            ax.scatter(centers[i][0], centers[i][1], c='black', s=100, marker='X')

        plt.gca().invert_yaxis()
        #plt.gca().invert_xaxis()
        plt.savefig(self.__figure_fn__)
        plt.close()
        print('done')
