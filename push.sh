case $1 in
    a|A|all|ALL|y|Y|yes|Yes)
        git pull
		echo ----------------------------------
        ;;
esac
git add *
echo ----------------------------------
echo -n "Continue?[y]/n"
read ANS
case $ANS in
    n|N|no|No)
        exit 0
        ;;
esac
git commit -m "$2"
echo ----------------------------------
git push
echo ----------------------------------
echo -n "Push huawei & github?[y]/n"
case $1 in
    a|A|all|ALL)
        ;;
	*)
		read ANS2
		case $ANS2 in
			n|N|no|No)
				exit 0
				;;
		esac
		;;
esac
git push huawei
echo ----------------------------------
git push github