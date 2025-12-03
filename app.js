const API = "/api";
let token = null;
let role = null;

function show(id, show=true){ document.getElementById(id).style.display = show ? 'block' : 'none'; }

async function register(){
  const u=document.getElementById('regUser').value, p=document.getElementById('regPass').value, r=document.getElementById('regRole').value;
  const res = await fetch(API + '/auth/register', {method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify({username:u,password:p,role:r})});
  alert(await res.text());
}

async function login(){
  const u=document.getElementById('loginUser').value, p=document.getElementById('loginPass').value;
  const res = await fetch(API + '/auth/login',{method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify({username:u,password:p})});
  if(res.ok){
    const data = await res.json();
    token = data.token; role = data.role;
    document.getElementById('who').innerText = u + ' ('+role+')';
    document.getElementById('auth').style.display='none';
    document.getElementById('app').style.display='block';
    if(role === 'EMPLOYER') show('employerPanel', true);
    listJobs();
    listMyApps();
    if(role==='EMPLOYER') listMyJobs();
  } else alert('Login failed');
}

function logout(){ token=null; role=null; document.getElementById('auth').style.display='block'; document.getElementById('app').style.display='none'; }

async function createJob(){
  const job={title:document.getElementById('jobTitle').value,company:document.getElementById('jobCompany').value,location:document.getElementById('jobLocation').value,salary:document.getElementById('jobSalary').value,description:document.getElementById('jobDesc').value};
  const res = await fetch(API + '/jobs',{method:'POST', headers:{'Content-Type':'application/json','Authorization':'Bearer '+token}, body: JSON.stringify(job)});
  if(res.ok) { alert('Created'); listMyJobs(); } else alert('Error');
}

async function listJobs(){
  const q=document.getElementById('q').value || '';
  const res = await fetch(API + '/jobs?q='+encodeURIComponent(q));
  const page = await res.json();
  document.getElementById('jobs').innerHTML = page.content.map(j => `<div>
    <h4>${j.title} - ${j.company}</h4>
    <div>${j.location} | ${j.salary}</div>
    <p>${j.description?.substring(0,200) || ''}</p>
    <button onclick="showApply(${j.id})">Apply</button>
    ${role==='EMPLOYER' ? `<button onclick="viewApplicants(${j.id})">View Applicants</button>` : ''}
  </div><hr/>`).join('');
}

function showApply(id){ document.getElementById('applyJobId').innerText=id; show('applyPanel',true); }

async function applyToJob(){
  const jobId = document.getElementById('applyJobId').innerText;
  const resume = document.getElementById('resume').files[0];
  const cover = document.getElementById('cover').value;
  const fd = new FormData(); fd.append('jobId', jobId); fd.append('resume', resume); fd.append('coverLetter', cover);
  const res = await fetch(API + '/applications/apply', {method:'POST', headers: {'Authorization':'Bearer '+token}, body: fd});
  if(res.ok){ alert('Applied'); listMyApps(); } else alert('Error applying');
}

async function listMyApps(){
  if(!token) return;
  const res = await fetch(API + '/applications/me', {headers:{'Authorization':'Bearer '+token}});
  if(res.ok){ const arr = await res.json(); document.getElementById('myApps').innerHTML = arr.map(a => `<div>Job: ${a.job.title} | Status: ${a.status}</div>`).join('<hr/>'); }
}

async function listMyJobs(){
  const res = await fetch(API + '/jobs/my', {headers:{'Authorization':'Bearer '+token}});
  if(res.ok){ const arr = await res.json(); document.getElementById('myJobs').innerHTML = arr.map(j=>`<div>${j.title} - <button onclick="viewApplicants(${j.id})">Applicants</button></div>`).join(''); }
}

async function viewApplicants(jobId){
  const res = await fetch(API + '/applications/job/' + jobId, {headers:{'Authorization':'Bearer '+token}});
  if(res.ok){ const arr = await res.json(); alert('Applicants: ' + arr.length); }
}
