// This is a part of the Microsoft Foundation Classes C++ library.
// Copyright (C) Microsoft Corporation
// All rights reserved.
//
// This source code is only intended as a supplement to the
// Microsoft Foundation Classes Reference and related
// electronic documentation provided with the library.
// See these sources for detailed information regarding the
// Microsoft Foundation Classes product.


#ifndef __CARRAY_H__
#define __CARRAY_H__


/////////////////////////////////////////////////////////////////////////////
// global helpers (can be overridden)


#pragma push_macro("new")


#include <string.h>

#ifndef AFXAPI
    #define AFXAPI _stdcall
#endif



typedef unsigned char uint8;
//#define assert(a) printf ("assert %s.\n",(a)?"atrue":"afalse")
//#define assert(a) printf ("ensure %s.\n",(a)?"etrue":"efalse")


template<class TYPE>
inline void CopyElements(TYPE* pDest, const TYPE* pSrc, int nCount)
{
    assert(nCount == 0 || pDest != 0 && pSrc != 0);


// default is element-copy using assignment
    while (nCount--)
        *pDest++ = *pSrc++;
}


/*============================================================================*/
// CArray<TYPE, ARG_TYPE>


template<class TYPE, class ARG_TYPE = const TYPE&>
class CArray
{
public:
// Construction
    CArray();


// Attributes
    int GetSize() const;
    int GetCount() const;

    bool IsEmpty() const;
    int GetUpperBound() const;
    void SetSize(int nNewSize, int nGrowBy = -1);


// Operations
// Clean up
    void FreeExtra();
    void RemoveAll();


// Accessing elements
    const TYPE& GetAt(int nIndex) const;
    TYPE& GetAt(int nIndex);
    void SetAt(int nIndex, ARG_TYPE newElement);
    const TYPE& ElementAt(int nIndex) const;
    TYPE& ElementAt(int nIndex);


// Direct Access to the element data (may return NULL)
    const TYPE* GetData() const;
    TYPE* GetData();


// Potentially growing the array
    void SetAtGrow(int nIndex, ARG_TYPE newElement);
    int Add(ARG_TYPE newElement);
    int Append(const CArray& src);
    void Copy(const CArray& src);


// overloaded operator helpers
    const TYPE& operator[](int nIndex) const;
    TYPE& operator[](int nIndex);


// Operations that move elements around
    void InsertAt(int nIndex, ARG_TYPE newElement, int nCount = 1);
    void RemoveAt(int nIndex, int nCount = 1);
    void InsertAt(int nStartIndex, CArray* pNewArray);


// Implementation
protected:
    TYPE* m_pData;// the actual array of data
    int m_nSize;// # of elements (upperBound - 1)
    int m_nMaxSize;// max allocated
    int m_nGrowBy;// grow amount


public:
    ~CArray();
#ifdef _DEBUG
    void Serialize(CArchive&);


void Dump(CDumpContext&) const;
void AssertValid() const;
#endif
};


inline void AfxThrowInvalidArgException (void)
{
    printf ("invalid arg.\n");
}


/*============================================================================*/
// CArray<TYPE, ARG_TYPE> inline functions


template<class TYPE, class ARG_TYPE>
inline int CArray<TYPE, ARG_TYPE>::GetSize() const
{ return m_nSize; }
template<class TYPE, class ARG_TYPE>
inline int CArray<TYPE, ARG_TYPE>::GetCount() const
{ return m_nSize; }
template<class TYPE, class ARG_TYPE>
inline bool CArray<TYPE, ARG_TYPE>::IsEmpty() const
{ return m_nSize == 0; }
template<class TYPE, class ARG_TYPE>
inline int CArray<TYPE, ARG_TYPE>::GetUpperBound() const
{ return m_nSize-1; }
template<class TYPE, class ARG_TYPE>
inline void CArray<TYPE, ARG_TYPE>::RemoveAll()
{ SetSize(0, -1); }
template<class TYPE, class ARG_TYPE>
inline TYPE& CArray<TYPE, ARG_TYPE>::GetAt(int nIndex)
{
    assert(nIndex >= 0 && nIndex < m_nSize);
    if(nIndex >= 0 && nIndex < m_nSize)
        return m_pData[nIndex];
    AfxThrowInvalidArgException();
}
template<class TYPE, class ARG_TYPE>
inline const TYPE& CArray<TYPE, ARG_TYPE>::GetAt(int nIndex) const
{
    assert(nIndex >= 0 && nIndex < m_nSize);
    if(nIndex >= 0 && nIndex < m_nSize)
        return m_pData[nIndex];
    AfxThrowInvalidArgException();
}
template<class TYPE, class ARG_TYPE>
inline void CArray<TYPE, ARG_TYPE>::SetAt(int nIndex, ARG_TYPE newElement)
{
    assert(nIndex >= 0 && nIndex < m_nSize);
    if(nIndex >= 0 && nIndex < m_nSize)
        m_pData[nIndex] = newElement;
    else
        AfxThrowInvalidArgException();
}
template<class TYPE, class ARG_TYPE>
inline const TYPE& CArray<TYPE, ARG_TYPE>::ElementAt(int nIndex) const
{
    assert(nIndex >= 0 && nIndex < m_nSize);
    if(nIndex >= 0 && nIndex < m_nSize)
        return m_pData[nIndex];
    AfxThrowInvalidArgException();
}
template<class TYPE, class ARG_TYPE>
inline TYPE& CArray<TYPE, ARG_TYPE>::ElementAt(int nIndex)
{
    assert(nIndex >= 0 && nIndex < m_nSize);
    if(nIndex >= 0 && nIndex < m_nSize)
        return m_pData[nIndex];
    AfxThrowInvalidArgException();
}
template<class TYPE, class ARG_TYPE>
inline const TYPE* CArray<TYPE, ARG_TYPE>::GetData() const
{ return (const TYPE*)m_pData; }
template<class TYPE, class ARG_TYPE>
inline TYPE* CArray<TYPE, ARG_TYPE>::GetData()
{ return (TYPE*)m_pData; }
template<class TYPE, class ARG_TYPE>
inline int CArray<TYPE, ARG_TYPE>::Add(ARG_TYPE newElement)
{ int nIndex = m_nSize;
    SetAtGrow(nIndex, newElement);
    return nIndex; }
template<class TYPE, class ARG_TYPE>
inline const TYPE& CArray<TYPE, ARG_TYPE>::operator[](int nIndex) const
{ return GetAt(nIndex); }
template<class TYPE, class ARG_TYPE>
inline TYPE& CArray<TYPE, ARG_TYPE>::operator[](int nIndex)
{ return ElementAt(nIndex); }


/*============================================================================*/
// CArray<TYPE, ARG_TYPE> out-of-line functions


template<class TYPE, class ARG_TYPE>
CArray<TYPE, ARG_TYPE>::CArray()
{
    m_pData = NULL;
    m_nSize = m_nMaxSize = m_nGrowBy = 0;
}


template<class TYPE, class ARG_TYPE>
CArray<TYPE, ARG_TYPE>::~CArray()
{
//assert_valid(this);


    if (m_pData != NULL)
    {
        for( int i = 0; i < m_nSize; i++ )
            (m_pData + i)->~TYPE();
        delete[] (uint8*)m_pData;
    }
}


template<class TYPE, class ARG_TYPE>
void CArray<TYPE, ARG_TYPE>::SetSize(int nNewSize, int nGrowBy)
{
//assert_valid(this);
    assert(nNewSize >= 0);


    if(nNewSize < 0 )
        AfxThrowInvalidArgException();


    if (nGrowBy >= 0)
        m_nGrowBy = nGrowBy;// set new size


    if (nNewSize == 0)
    {
// shrink to nothing
        if (m_pData != NULL)
        {
            for( int i = 0; i < m_nSize; i++ )
                (m_pData + i)->~TYPE();
            delete[] (uint8*)m_pData;
            m_pData = NULL;
        }
        m_nSize = m_nMaxSize = 0;
    }
    else if (m_pData == NULL)
    {
// create buffer big enough to hold number of requested elements or
// m_nGrowBy elements, whichever is larger.
#ifdef SIZE_T_MAX
        assert(nNewSize <= SIZE_T_MAX/sizeof(TYPE)); // no overflow
#endif
        size_t nAllocSize = std::max (nNewSize, m_nGrowBy);
        m_pData = (TYPE*) new uint8[(size_t)nAllocSize * sizeof(TYPE)];
        memset((void*)m_pData, 0, (size_t)nAllocSize * sizeof(TYPE));
        for( int i = 0; i < nNewSize; i++ )
#pragma push_macro("new")
#undef new
                ::new( (void*)( m_pData + i ) ) TYPE;
#pragma pop_macro("new")
        m_nSize = nNewSize;
        m_nMaxSize = nAllocSize;
    }
    else if (nNewSize <= m_nMaxSize)
    {
// it fits
        if (nNewSize > m_nSize)
        {
// initialize the new elements
            memset((void*)(m_pData + m_nSize), 0, (size_t)(nNewSize-m_nSize) * sizeof(TYPE));
            for( int i = 0; i < nNewSize-m_nSize; i++ )
#pragma push_macro("new")
#undef new
                    ::new( (void*)( m_pData + m_nSize + i ) ) TYPE;
#pragma pop_macro("new")
        }
        else if (m_nSize > nNewSize)
        {
// destroy the old elements
            for( int i = 0; i < m_nSize-nNewSize; i++ )
                (m_pData + nNewSize + i)->~TYPE();
        }
        m_nSize = nNewSize;
    }
    else
    {
// otherwise, grow array
        nGrowBy = m_nGrowBy;
        if (nGrowBy == 0)
        {
// heuristically determine growth when nGrowBy == 0
//  (this avoids heap fragmentation in many situations)
            nGrowBy = m_nSize / 8;
            nGrowBy = (nGrowBy < 4) ? 4 : ((nGrowBy > 1024) ? 1024 : nGrowBy);
        }
        int nNewMax;
        if (nNewSize < m_nMaxSize + nGrowBy)
            nNewMax = m_nMaxSize + nGrowBy; // granularity
        else
        nNewMax = nNewSize; // no slush


        assert(nNewMax >= m_nMaxSize); // no wrap around


        if(nNewMax < m_nMaxSize)
        AfxThrowInvalidArgException();


#ifdef SIZE_T_MAX
        assert(nNewMax <= SIZE_T_MAX/sizeof(TYPE)); // no overflow
#endif
        TYPE* pNewData = (TYPE*) new uint8[(size_t)nNewMax * sizeof(TYPE)];


// copy new data from old
        memcpy(pNewData, m_pData, (size_t)m_nSize * sizeof(TYPE));


// construct remaining elements
        assert(nNewSize > m_nSize);
        memset((void*)(pNewData + m_nSize), 0, (size_t)(nNewSize-m_nSize) * sizeof(TYPE));
        for( int i = 0; i < nNewSize-m_nSize; i++ )
#pragma push_macro("new")
#undef new
                ::new( (void*)( pNewData + m_nSize + i ) ) TYPE;
#pragma pop_macro("new")


// get rid of old stuff (note: no destructors called)
        delete[] (uint8*)m_pData;
        m_pData = pNewData;
        m_nSize = nNewSize;
        m_nMaxSize = nNewMax;
    }
}


template<class TYPE, class ARG_TYPE>
int CArray<TYPE, ARG_TYPE>::Append(const CArray& src)
{
//assert_valid(this);
    assert(this != &src); // cannot append to itself


    if(this == &src)
        AfxThrowInvalidArgException();


    int nOldSize = m_nSize;
    SetSize(m_nSize + src.m_nSize);
    CopyElements<TYPE>(m_pData + nOldSize, src.m_pData, src.m_nSize);
    return nOldSize;
}


template<class TYPE, class ARG_TYPE>
void CArray<TYPE, ARG_TYPE>::Copy(const CArray& src)
{
//assert_valid(this);
    assert(this != &src); // cannot append to itself


    if(this != &src)
    {
        SetSize(src.m_nSize);
        CopyElements<TYPE>(m_pData, src.m_pData, src.m_nSize);
    }
}


template<class TYPE, class ARG_TYPE>
void CArray<TYPE, ARG_TYPE>::FreeExtra()
{
//assert_valid(this);


    if (m_nSize != m_nMaxSize)
    {
// shrink to desired size
#ifdef SIZE_T_MAX
        assert(m_nSize <= SIZE_T_MAX/sizeof(TYPE)); // no overflow
#endif
        TYPE* pNewData = NULL;
        if (m_nSize != 0)
        {
            pNewData = (TYPE*) new uint8[m_nSize * sizeof(TYPE)];
// copy new data from old
            memcpy(pNewData, m_pData, m_nSize * sizeof(TYPE));
        }


// get rid of old stuff (note: no destructors called)
        delete[] (uint8*)m_pData;
        m_pData = pNewData;
        m_nMaxSize = m_nSize;
    }
}


template<class TYPE, class ARG_TYPE>
void CArray<TYPE, ARG_TYPE>::SetAtGrow(int nIndex, ARG_TYPE newElement)
{
//assert_valid(this);
    assert(nIndex >= 0);


    if(nIndex < 0)
        AfxThrowInvalidArgException();


    if (nIndex >= m_nSize)
        SetSize(nIndex+1, -1);
    m_pData[nIndex] = newElement;
}


template<class TYPE, class ARG_TYPE>
void CArray<TYPE, ARG_TYPE>::InsertAt(int nIndex, ARG_TYPE newElement, int nCount /*=1*/)
{
//assert_valid(this);
    assert(nIndex >= 0); // will expand to meet need
    assert(nCount > 0); // zero or negative size not allowed


    if(nIndex < 0 || nCount <= 0)
        AfxThrowInvalidArgException();


    if (nIndex >= m_nSize)
    {
// adding after the end of the array
        SetSize(nIndex + nCount, -1); // grow so nIndex is valid
    }
    else
    {
// inserting in the middle of the array
        int nOldSize = m_nSize;
        SetSize(m_nSize + nCount, -1); // grow it to new size
// destroy intial data before copying over it
        for( int i = 0; i < nCount; i++ )
            (m_pData + nOldSize + i)->~TYPE();
// shift old data up to fill gap
        memmove(m_pData + nIndex + nCount, m_pData + nIndex, (nOldSize-nIndex) * sizeof(TYPE));


// re-init slots we copied from
        memset((void*)(m_pData + nIndex), 0, (size_t)nCount * sizeof(TYPE));
        for( int i = 0; i < nCount; i++ )
#pragma push_macro("new")
#undef new
                ::new( (void*)( m_pData + nIndex + i ) ) TYPE;
#pragma pop_macro("new")
    }


// insert new value in the gap
    assert(nIndex + nCount <= m_nSize);
    while (nCount--)
        m_pData[nIndex++] = newElement;
}


template<class TYPE, class ARG_TYPE>
void CArray<TYPE, ARG_TYPE>::RemoveAt(int nIndex, int nCount)
{
//assert_valid(this);
    assert(nIndex >= 0);
    assert(nCount >= 0);
    int nUpperBound = nIndex + nCount;
    assert(nUpperBound <= m_nSize && nUpperBound >= nIndex && nUpperBound >= nCount);


    if(nIndex < 0 || nCount < 0 || (nUpperBound > m_nSize) || (nUpperBound < nIndex) || (nUpperBound < nCount))
        AfxThrowInvalidArgException();


// just remove a range
    int nMoveCount = m_nSize - (nUpperBound);
    for( int i = 0; i < nCount; i++ )
        (m_pData + nIndex + i)->~TYPE();
    if (nMoveCount)
    {
        memmove(m_pData + nIndex, m_pData + nUpperBound, (size_t)nMoveCount * sizeof(TYPE));
    }
    m_nSize -= nCount;
}


template<class TYPE, class ARG_TYPE>
void CArray<TYPE, ARG_TYPE>::InsertAt(int nStartIndex, CArray* pNewArray)
{
//assert_valid(this);
    assert(pNewArray != NULL);
    assert_valid(pNewArray);
    assert(nStartIndex >= 0);


    if(pNewArray == NULL || nStartIndex < 0)
        AfxThrowInvalidArgException();


    if (pNewArray->GetSize() > 0)
    {
        InsertAt(nStartIndex, pNewArray->GetAt(0), pNewArray->GetSize());
        for (int i = 0; i < pNewArray->GetSize(); i++)
            SetAt(nStartIndex + i, pNewArray->GetAt(i));
    }
}
#ifdef _DEBUG


template<class TYPE, class ARG_TYPE>
void CArray<TYPE, ARG_TYPE>::Serialize(CArchive& ar)
{
//assert_valid(this);


CObject::Serialize(ar);
if (ar.IsStoring())
{
ar.WriteCount(m_nSize);
}
else
{
DWORD_PTR nOldSize = ar.ReadCount();
SetSize(nOldSize, -1);
}
SerializeElements<TYPE>(ar, m_pData, m_nSize);
}


template<class TYPE, class ARG_TYPE>
void CArray<TYPE, ARG_TYPE>::Dump(CDumpContext& dc) const
{
CObject::Dump(dc);


dc << "with " << m_nSize << " elements";
if (dc.GetDepth() > 0)
{
dc << "\n";
DumpElements<TYPE>(dc, m_pData, m_nSize);
}


dc << "\n";
}


template<class TYPE, class ARG_TYPE>
void CArray<TYPE, ARG_TYPE>::AssertValid() const
{
CObject::AssertValid();


if (m_pData == NULL)
{
assert(m_nSize == 0);
assert(m_nMaxSize == 0);
}
else
{
assert(m_nSize >= 0);
assert(m_nMaxSize >= 0);
assert(m_nSize <= m_nMaxSize);
}
}
#endif //_DEBUG






/////////////////////////////////////////////////////////////////////////////


#pragma pop_macro("new")


#endif //__CARRAY_H__


/////////////////////////////////////////////////////////////////////////////
