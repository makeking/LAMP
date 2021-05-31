package com.myutils;

import static java.lang.Math.sqrt;
import static java.lang.StrictMath.abs;

public class AlgorithmUtils {
    //最小二乘拟合
    static public void Jbsqt(double []x,double[] y,int n,double[] a,double[] dt)
// double x[],y[],a[2],dt[6];
    {
        int i;
        double xx,yy,e,f,q,u,p,umax,umin,s;
        xx=0; yy=0;
        for (i=0; i<=n-1; i++)
        {
            xx += x[i]/n;
            yy += y[i]/n;
        }
        e=0.0; f=0.0;
        for (i=0; i<=n-1; i++)
        {
            q=x[i]-xx; e += q*q;
            f += q*(y[i]-yy);
        }
        a[1]=f/e;
        a[0]=yy-a[1]*xx;
        q=0.0; u=0.0; p=0.0;
        umax=0.0; umin=1.0e+30;
        for (i=0; i<=n-1; i++)
        {
            s=a[1]*x[i]+a[0];
            q += (y[i]-s)*(y[i]-s);
            p += (s-yy)*(s-yy);
            e=abs(y[i]-s);
            if (e>umax) umax=e;
            if (e<umin) umin=e;
            u += e/n;
        }
        dt[1]=sqrt(q/n);
        dt[0]=q; dt[2]=p;
        dt[3]=umax;
        dt[4]=umin;
        dt[5]=u;
        return;
    }
    //线性拟合
    static public void LinearFit(double[] x, double[] y,int nNum, double[] dReturn)
    {
        double[] dt= new double[6];
        double[] a= new double[2];
        Jbsqt(x,y,nNum,a,dt);
        dReturn[0] = a[0];
        dReturn[1] = a[1];

        /////////////////R2////////////////////
        double xMean = 0, yMean = 0;
        for(int i=0; i<nNum; i++)
        {
            xMean += x[i];
            yMean += y[i];
        } // for(int i=0; i<nNum; i++)
        xMean /= nNum;
        yMean /= nNum;
        double aUp =0, bDown=0, cDown =0;
        for(int i=0; i<nNum; i++)
        {
            aUp += abs((x[i]-xMean)*(y[i]-yMean));
            bDown += (x[i]-xMean)*(x[i]-xMean);
            cDown += (y[i]-yMean)*(y[i]-yMean);
        } // for(i=0; i<nNum; i++)

        //////////////////R///////////////////////
        double dSumXY = 0,dSumX = 0, dSumY = 0, dSumX2 = 0, dSumY2 = 0;
        for(int i=0; i< nNum; i++)
        {
            dSumXY += x[i]*y[i];
            dSumX += x[i];
            dSumY += y[i];
            dSumX2 += x[i]*x[i];
            dSumY2 += y[i]*y[i];
        }
        double dUp = nNum*dSumXY - dSumX * dSumY;
        double dDown = sqrt( nNum*dSumX2 - dSumX*dSumX) * sqrt( nNum*dSumY2 - dSumY*dSumY);

        //////////////////////////////////////////
        //	if( bDown!= 0 && cDown!= 0)
        //		dReturn[2] = aUp/(sqrt(bDown*cDown));
        if( dDown != 0)
            dReturn[2] = dUp /dDown;
        else
            dReturn[2] = 0;
    }
    //滤波
    static public void Kdspt_cfmean(int n,int cfnum,int []y,int []yy)
    {
        //中心滑动平均滤波 dtnum(滑动窗口为奇数)
        int i;
        int k;
        int sum;
        int halfwindow;
        if ((cfnum<3)||(cfnum%2 != 1))
        {
            for (i=0; i<=n-1; i++)
                yy[i]=y[i];
        }
        else
        {
            halfwindow = cfnum/2;
            int beginindex = 0;
            int endindex = 0;
            for ( i = 0; i <= n - 1; i++ )
            {
                if (n < cfnum)
                {
                    if ( i < cfnum-1)
                    {
                        k = i;
                    }
                    else
                    {
                        k = cfnum-1;
                    }
                    sum = 0;
                    for ( int j = 0 ; j <= k; j++ )
                    {
                        sum += y[i-k+j];
                    }
                    yy[i] = sum/(k+1);
                }
                else
                {
                    if (i >= halfwindow)
                    {
                        beginindex = i-halfwindow;
                    }
                    else
                    {
                        beginindex = 0;
                    }
                    int nPre = i - beginindex ;
                    if (i+halfwindow <= n-1)
                    {
                        endindex = i+halfwindow;
                    }
                    else
                    {
                        endindex = n-1;
                    }
                    int nBehind = endindex - i;
                    if ((nPre > nBehind)) {
                        beginindex = i - nBehind;
                    } else {
                        endindex = i + nPre;
                    }
                    sum = 0;
                    for ( int j = beginindex ; j <= endindex; j++ )
                    {
                        sum += y[j];
                    }
                    yy[i] = sum/(endindex-beginindex+1);
                }
            }
        }

        return;
    }

}
