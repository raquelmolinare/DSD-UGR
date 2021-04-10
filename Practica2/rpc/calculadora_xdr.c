/*
 * Please do not edit this file.
 * It was generated using rpcgen.
 */

#include "calculadora.h"

bool_t
xdr_operationBasic (XDR *xdrs, operationBasic *objp)
{
	register int32_t *buf;

	 if (!xdr_double (xdrs, &objp->first))
		 return FALSE;
	 if (!xdr_double (xdrs, &objp->second))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_responseBasic (XDR *xdrs, responseBasic *objp)
{
	register int32_t *buf;

	 if (!xdr_int (xdrs, &objp->error))
		 return FALSE;
	switch (objp->error) {
	case 0:
		 if (!xdr_double (xdrs, &objp->responseBasic_u.result))
			 return FALSE;
		break;
	default:
		break;
	}
	return TRUE;
}

bool_t
xdr_vectorData (XDR *xdrs, vectorData *objp)
{
	register int32_t *buf;

	 if (!xdr_s_vector (xdrs, objp))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_s_vector (XDR *xdrs, s_vector *objp)
{
	register int32_t *buf;

	 if (!xdr_int (xdrs, &objp->vectorDim))
		 return FALSE;
	 if (!xdr_pointer (xdrs, (char **)&objp->vectorValues, sizeof (double), (xdrproc_t) xdr_double))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_responseVectores (XDR *xdrs, responseVectores *objp)
{
	register int32_t *buf;

	 if (!xdr_int (xdrs, &objp->error))
		 return FALSE;
	switch (objp->error) {
	case 0:
		 if (!xdr_vectorData (xdrs, &objp->responseVectores_u.v))
			 return FALSE;
		break;
	default:
		break;
	}
	return TRUE;
}

bool_t
xdr_sumavectores_1_argument (XDR *xdrs, sumavectores_1_argument *objp)
{
	 if (!xdr_vector (xdrs, &objp->v1))
		 return FALSE;
	 if (!xdr_vector (xdrs, &objp->v2))
		 return FALSE;
	return TRUE;
}
