import {react,use,useEffect,useState} from 'react'
import '../../css/MyApplications.css'
import api from '../../config/axiosConfig'
export default function MyApplications(){
    const [application,setApplication]= useState([])
    const [loading, setLoading] = useState(false)
     const role = sessionStorage.getItem("role");
    useEffect(()=>
        {fetchApplications();
            
        },[])

    const fetchApplications = async() =>{

        setLoading(true);
        let res;
        try{
            if(role === "STUDENT")
            {
                const res = await api.get("/api/applications/me")
                console.log(res.data)
                setApplication(res.data);
            }
            
        }catch(err)
        {
            console.log(err)
            alert("Failed to load applications")
        }finally {
      setLoading(false);
    }
    }
    return (
    <div className="app-container">
    
      {
        loading ?(
             <p className="no-data">Loading Applications...</p>
        ) :(
            <div>{
                application.length > 0 ?(
                    application.map(
                        (app)=>(
                            <div key={app.id} className='app-card'>
                                 <p><strong>Application Id : </strong>{app.id}</p>
                                <p><strong>Role : </strong>{app.job.role}</p>
                                 <p><strong>Application Status : </strong>{app.status}</p>
                                 <p><strong>Resume Id : </strong>{app.resumeId}</p>
                                <p><strong>Applied At : </strong>{new Date(app.appliedAt).toLocaleDateString()}</p>
                                <p><strong>Deadline : </strong>{new Date(app.job.deadline).toLocaleDateString()}</p>
                            </div>
                        )
                    )
                ):(
                    <div>No Data Found</div>
                )
                }
               
            </div>
        )
      }
      </div>
      );

};