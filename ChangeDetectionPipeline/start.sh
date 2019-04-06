#!/bin/bash
: 'Written by Giuseppe Calabrese, Computer Science Student, University of Bari
	email: calabrese.giuseppe94@gmail.com'

function print_help {
	echo 'USAGE: start.sh [RUN_ID] [DS0] [DS1] [NAME] [K] [SILHOUETTE] [PLOT]'
	echo '[RUN_ID] : the id of this program run'
	echo '[DS0] : the .mat data set filename at t0 time point'
	echo '[DS1] : the .mat data set filename at t0 time point'
	echo '[NAME] : the name of the matrix containing files in the .mat files'
	echo '[K] : (positive) number of centers to use in the clustering operation'
	echo '[SILHOUETTE] : y if Silhouette index is required, <any other character> otherwise'
	echo '[PLOT]: y if scatter plots are required, <any other character> otherwise'
}

function print_error {
	echo 'The RUN_ID you have entered is alreay used. '
	echo 'Please execute the script again using another id'
}

function find_last_iteration {
	local dirlist=$(find $1 -mindepth 1 -maxdepth 1 -type d) #take directories in $s3out
	for dir in $dirlist; do
		IFS='/' read -ra ADDR <<< "$dir" 
		for i in "${ADDR[@]}"; do
	    	last=$i
		done
		lis+=("$last")	
	done
	local i=0
	local max=0
	for i in ${#lis[@]}; do
		if [ '$lis[$i]' > $max ]; then
			max=$i
		fi
	done
	echo $max
}

function move_to_outfolder {
	mv $s4out/changes.txt $final_out
	mv $s3out/$last/part-r-00000 $final_out/final_centers
	mv $s2out/initial_centers.txt $final_out/initial_centers
	mv $s5out/hadoop_kmeans.txt $final_out/hadoop_kmeans
	rm -rf ./input 
	if [ $1 = 'y' ]; then
		mv $s5out/silhouette.txt $final_out/silhouette
	fi
	if [ $2 = 'y' ]; then
		mv $s6out/plot_t0 $final_out/plot_t0
		mv $s6out/plot_t1 $final_out/plot_t1
	fi
}

if [ $# -eq 0 ] || [ $1 = '-h' ] || [ $1 = '--help' ] ; then
	print_help
else
	runid=$1
	ds0=$2
	ds1=$3
	matname=$4
	k=$5
	silh=$6
	plot=$7

	if [ $k = '0' ]; then
		echo "K parameter can't be less than 1"
		exit
	fi

	s1out=$runid/s1out
	s2out=$runid/s2out
	s3out=$runid/s3out
	s4out=$runid/s4out
	s5out=$runid/s5out
	s6out=$runid/s6out
	final_out=$runid

	if ! mkdir $runid; then
		print_error
		exit
	fi
	mkdir $s1out
	mkdir $s2out
	mkdir $s3out
	mkdir $s4out
	mkdir $s5out
	mkdir $s6out

	echo please wait, it can take a long time
	#stage 1 
	echo stage 1 out of 6 has started
	python3 ./programs/matToTxt.py $ds0 $matname 0 $s1out/dataset_t0.txt
	python3 ./programs/matToTxt.py $ds1 $matname 0 $s1out/dataset_t1.txt
	python3 ./programs/concat.py $s1out/dataset_t0.txt $s1out/dataset_t1.txt $s1out/cd_dataset.txt
	#stage 2
	echo stage 2 out of 6 has started
	java -jar ./programs/initialCenters.jar $k $s1out/cd_dataset.txt $s2out/initial_centers.txt
	#stage 3
	echo stage 3 out of 6 has started
	java -cp ./programs/jgalilee.jar com.jgalilee.hadoop.kmeans.driver.Driver $s2out/kmeans.state $s1out/cd_dataset.txt $s2out/initial_centers.txt 1 $s3out 0.0 400 &> $s5out/hadoop_kmeans.txt
	#stage 4
	echo stage 4 out of 6 has started
	last=$(find_last_iteration $s3out)
	java -jar ./programs/changeDetector.jar $s1out/cd_dataset.txt $s3out/$last/part-r-00000 $s4out/changes.txt
	#stage 5
	echo stage 5 out of 6 has started
	if [ $silh = 'y' ]; then
		java -jar -Xms4g -Xms7g ./programs/silhouetteCalculator.jar -j $s1out/cd_dataset.txt  $s5out/silhouette.txt  $s3out/$last/part-r-00000
	fi
	echo stage 6 out of 6 has started
	if [ $plot = 'y' ]; then
		python3 ./programs/jgalileePlotter.py $s3out/$last/part-r-00000 $s1out/dataset_t0.txt 0 $s6out/plot_t0 False 
		python3 ./programs/jgalileePlotter.py $s3out/$last/part-r-00000 $s1out/dataset_t1.txt 0 $s6out/plot_t1 False
	fi
	#copy to output folder
	echo just a moment
	move_to_outfolder $silh $plot
	#remove working dirs
	rm -rf $s1out
	rm -rf $s2out
	rm -rf $s3out
	rm -rf $s4out
	rm -rf $s5out
	rm -rf $s6out
	echo finished
fi
